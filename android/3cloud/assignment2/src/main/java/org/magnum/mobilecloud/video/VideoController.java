package org.magnum.mobilecloud.video;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

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
	
	private Map<Integer,List<String>> userLike = new HashMap<Integer,List<String>>();
	
	@Autowired
	private VideoRepository videoRepository;

	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
	@ResponseBody
	public Collection<Video> getVideoList() {
		return Lists.newArrayList(videoRepository.findAll());
	}


	public Video getVideoById(long id) {
		return videoRepository.findOne(id);
	}

	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
	@ResponseBody
	public Video addVideo(@RequestBody Video v) {
		Video video = videoRepository.save(v);
		return video;
	}

	public Void likeVideo(long id,HttpServletRequest request) {
		Video video = videoRepository.findOne(id);
		Long likes = video.getLikes();
		video.setLikes(likes + 1);
		videoRepository.save(video);
		request.getUserPrincipal().getName();
		return null;
	}

	public Void unlikeVideo(long id) {
		Video video = videoRepository.findOne(id);
		Long likes = video.getLikes();
		video.setLikes(likes - 1);
		videoRepository.save(video);
		return null;
	}

	public Collection<Video> findByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Video> findByDurationLessThan(long duration) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<String> getUsersWhoLikedVideo(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
