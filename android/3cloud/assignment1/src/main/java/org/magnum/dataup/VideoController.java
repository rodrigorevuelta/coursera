/**
 * VideoController.java 31/07/2014
 *
 * Copyright 2014 INDITEX.
 * Departamento de Sistemas
 */
package org.magnum.dataup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    List<Video> videoList = new ArrayList<Video>();

    @Override
    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody
    Collection<Video> getVideoList() {
        return this.videoList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.PUT)
    public Video addVideo(final Video v) {
        this.videoList.add(v);
        v.setId(this.videoList.size());
        return v;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VideoStatus setVideoData(final long id, final TypedFile videoData) {
        // TODO Auto-generated method stub
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
