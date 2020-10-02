package edu.escuelaing.arep.secure;

import static spark.Spark.*;

public class SparkSecureApp {
    public static void main(String[] args) {
        port(getPort());
        secure("keystores/ecikeystore.p12","123456", null,null);
        get("/hello", (req, res) -> "Hello World");
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
