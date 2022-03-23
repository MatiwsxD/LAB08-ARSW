package edu.eci.arsw.collabpaint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.persistence.PointPersistenceExeption;
import edu.eci.arsw.collabpaint.persistence.PointsPersistence;

@Controller
public class STOMPMessagesHandler {

    @Autowired
    SimpMessagingTemplate msgt;

    @Autowired
    PointsPersistence memPo;

    @MessageMapping("/newpoint.{numdibujo}")
    public void handlePointEvent(Point pt,@DestinationVariable String numdibujo) throws Exception {
        System.out.println("Nuevo punto recibido en el servidor!:"+pt);
        msgt.convertAndSend("/topic/newpoint."+numdibujo, pt);
        try {
            msgt.convertAndSend("/topic/newpolygon."+numdibujo, memPo.getPolygon(numdibujo, pt));
        }catch(PointPersistenceExeption e) {
            System.out.println(e.getMessage());
        }
    }
}