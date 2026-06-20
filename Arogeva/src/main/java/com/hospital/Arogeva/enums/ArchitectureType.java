package com.hospital.Arogeva.enums;




public enum ArchitectureType {



    REACT_SPRING_BOOT("React + Spring Boot"),
    REACT_NODE_JS("React + Node.js"),
    ANGULAR_SPRING_BOOT("Angular + Spring Boot"),
    MVC("MVC"),
    SERVLET("Servlet"),
    MICROSERVICES("Microservices"),
    MONOLITHIC("Monolithic"),
    SERVERLESS("Serverless"),
    OTHER("Other");

    private final String value;

    ArchitectureType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
