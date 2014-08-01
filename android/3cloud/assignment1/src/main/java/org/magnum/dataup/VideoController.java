/**
 * VideoController.java 31/07/2014
 *
 * Copyright 2014 INDITEX.
 * Departamento de Sistemas
 */
package org.magnum.dataup;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * 
 * @author <a href="mailto:rodrigorr@servicioexterno.inditex.com">Rodrigo Revuelta Roca</a>
 * 
 */
@Controller
public class VideoController implements VideoSvcApi {

    List<Video> videoList = new CopyOnWriteArrayList<Video>();

    @Override
    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
    @ResponseBody
    public Collection<Video> getVideoList() {
        return this.videoList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
    @ResponseBody
    public Video addVideo(final Video v) {
        this.videoList.add(v);
        v.setId(this.videoList.size());
        return v;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = VIDEO_DATA_PATH, method = RequestMethod.POST)
    @ResponseBody
    public VideoStatus setVideoData(@RequestParam("id") final long id,
            @RequestParam("photo") final TypedFile videoData) {

        System.out.println(videoData.mimeType());
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getData(final long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
