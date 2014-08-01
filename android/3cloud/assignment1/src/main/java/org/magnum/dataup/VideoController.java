/**
 * VideoController.java 31/07/2014
 *
 * Copyright 2014 INDITEX.
 * Departamento de Sistemas
 */
package org.magnum.dataup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.client.Response;
import retrofit.http.Multipart;

/**
 * 
 * @author <a href="mailto:rodrigorr@servicioexterno.inditex.com">Rodrigo Revuelta Roca</a>
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
            // is.read()
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final VideoStatus status = new VideoStatus(VideoState.READY);
        return status;
    }

    /**
     * {@inheritDoc}
     */
    public Response getData(final long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
