package org.magnum.dataup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.http.Multipart;

/**
 * 
 * @author <a href="mailto:rodrigorevueltaroca@gmail.com">Rodrigo Revuelta Roca</a>
 * 
 */
@Controller
public class VideoController {

	public static final String DATA_PARAMETER = "data";
	public static final String ID_PARAMETER = "id";
	public static final String VIDEO_SVC_PATH = "/video";
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";

	List<Video> videoList = new CopyOnWriteArrayList<Video>();

	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
	@ResponseBody
	public Collection<Video> getVideoList() {
		return this.videoList;
	}

	/**
	 * {@inheritDoc}
	 */
	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
	@ResponseBody
	public Video addVideo(final @RequestBody Video v) {
		this.videoList.add(v);
		v.setId(this.videoList.size());
		v.setDataUrl(VIDEO_DATA_PATH + v.getId());
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Multipart
	@RequestMapping(value = VIDEO_DATA_PATH, method = RequestMethod.POST)
	@ResponseBody
	public VideoStatus setVideoData(@PathVariable(ID_PARAMETER) final long id,
			@RequestParam(DATA_PARAMETER) final MultipartFile videoData) {
		try {
			final InputStream is = videoData.getInputStream();
			Video v = new Video();
			v.setId(id);
			v.setDataUrl(VIDEO_DATA_PATH + id);
			boolean containsVideo = false;
			for (Video video : videoList) {
				if (video.getId() == id) {
					containsVideo = true;
				}
			}
			if (containsVideo) {
				VideoFileManager fileManager = VideoFileManager.get();
				fileManager.saveVideoData(v, is);

			} else {
				throw new ResourceNotFoundException();
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}
		final VideoStatus status = new VideoStatus(VideoState.READY);
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@RequestMapping(value = VIDEO_DATA_PATH, method = RequestMethod.GET)
	@ResponseBody
	public void getData(@PathVariable(ID_PARAMETER) final long id,
			HttpServletResponse response) {

		Video video = new Video();
		video.setId(id);
		try {
			VideoFileManager fileManager = VideoFileManager.get();
			if (fileManager.hasVideoData(video)) {
				VideoFileManager.get().copyVideoData(video,
						response.getOutputStream());
				response.flushBuffer();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
