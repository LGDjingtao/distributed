package org.example.firstactor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

public class IotMain {

    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("iot-system");

        try {
            // 创建顶级 actor
            ActorRef supervisor = system.actorOf(IotSupervisor.props(), "iot-supervisor");

            System.out.println("按 ENTER键 退出系统");
            System.in.read();
        } finally {
            system.terminate();
        }
    }
}