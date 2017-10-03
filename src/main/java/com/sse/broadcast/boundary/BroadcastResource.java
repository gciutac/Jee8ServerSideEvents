/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sse.broadcast.boundary;

import java.time.LocalTime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import javax.ejb.Schedule;
import javax.ejb.Singleton;


/**
 *
 * @author GHCIUTAC
 */
@Singleton
@Path("beats")
public class BroadcastResource {
    private SseBroadcaster broadcaster;
    private Sse sse;
    
    
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@Context Sse sse, @Context SseEventSink eventSink){
        this.sse = sse;
        if (broadcaster == null){
            this.broadcaster = sse.newBroadcaster();
        }
        else {
            this.broadcaster.register(eventSink);
        }
        System.out.println("Register ...");
    }
    
    @Schedule(second = "*/3", minute = "*", hour = "*")
    public void myBeat(){
        System.out.println("...");
        if (this.broadcaster != null) {
            this.broadcaster.broadcast(this.sse.newEvent("time: " + LocalTime.now().toString()));
        }
    }
}
