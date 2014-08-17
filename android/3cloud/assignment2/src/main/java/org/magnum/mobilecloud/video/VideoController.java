package org.magnum.mobilecloud.video;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

/**
 * 
 * @author <a href="mailto:rodrigorevueltaroca@gmail.com">Rodrigo Revuelta
 *         Roca</a>
 * 
 */
@Controller
public class VideoController {

	public static final String DATA_PARAMETER = "data";
	public static final String TITLE_PARAMETER = "title";
	public static final String ID_PARAMETER = "id";
	public static final String DURATION_PARAMETER = "duration";
	public static final String VIDEO_SVC_PATH = "/video";
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";
	public static final String VIDEO_LIKE = VIDEO_SVC_PATH + "/{id}/like";
	public static final String VIDEO_UNLIKE = VIDEO_SVC_PATH + "/{id}/unlike";
	// The path to search videos by title
	public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByName";
	public static final String VIDEO_LIKEDBY = VIDEO_SVC_PATH + "/{id}/likedby";
	// The path to search videos by title
	public static final String VIDEO_DURATION_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByDurationLessThan";

	private Map<Long, List<String>> usersLike = new HashMap<Long, List<String>>();

	@Autowired
	private VideoRepository videoRepository;

	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
	@ResponseBody
	public Collection<Video> getVideoList() {
		return Lists.newArrayList(videoRepository.findAll());
	}

	@RequestMapping(value = VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Video getVideoById(@PathVariable(ID_PARAMETER) long id) {
		return videoRepository.findOne(id);
	}

	@RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
	@ResponseBody
	public Video addVideo(@RequestBody Video v) {
		Video video = videoRepository.save(v);
		return video;
	}

	@RequestMapping(value = VIDEO_LIKE, method = RequestMethod.POST)
	@ResponseBody
	public Void likeVideo(@PathVariable(ID_PARAMETER) long id,
			Principal principal, HttpServletResponse resp) throws IOException {
		String userName = principal.getName();
		Video video = videoRepository.findOne(id);
		if (video == null) {
			resp.sendError(404, "Video not found");
		} else {
			if (usersLike.get(id) == null
					|| !usersLike.get(id).contains(userName)) {
				Long likes = video.getLikes();
				List<String> users = usersLike.get(id);
				if (users == null) {
					users = new ArrayList<String>();
					usersLike.put(id, users);
				}
				video.setLikes(likes + 1);
				videoRepository.save(video);
				users.add(userName);
			} else if (usersLike.get(id).contains(userName)) {
				resp.sendError(400, "User like this video");
			}
		}
		return null;
	}

	@RequestMapping(value = VIDEO_UNLIKE, method = RequestMethod.POST)
	@ResponseBody
	public Void unlikeVideo(@PathVariable(ID_PARAMETER) long id, Principal principal) {
		Video video = videoRepository.findOne(id);
		Long likes = video.getLikes();
		video.setLikes(likes - 1);
		videoRepository.save(video);
		usersLike.get(id).remove(principal.getName());
		return null;
	}

	@RequestMapping(value = VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	@ResponseBody
	public Collection<Video> findByTitle(
			@RequestParam(TITLE_PARAMETER) String title) {
		return videoRepository.findByName(title);
	}

	
	@RequestMapping(value = VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
	@ResponseBody
	public Collection<Video> findByDurationLessThan(@RequestParam(DURATION_PARAMETER) long duration) {
		return videoRepository.findByDurationLessThan(duration);
	}

	@RequestMapping(value = VIDEO_LIKEDBY, method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> getUsersWhoLikedVideo(@PathVariable(ID_PARAMETER) long id) {
		return usersLike.get(id);
	}

}
