package edu.escuelaing.arep.secure;

import java.io.UnsupportedEncodingException;
import java.util.*;

import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import static spark.Spark.*;

public class SparkSecureApp {
    public static void main(String[] args) {
        port(getPort());
        secure("keystores/ecikeystore.p12","123456", null,null);

        get("/hello", (req,res) ->{
            Map<String, Object> model = new HashMap<>();
            String response;
            if(req.session().attribute("Autenticado").toString().equals("true")){
                response = "Esta autenticado";
            }else{
                response = "No esta autenticado";
            }
            model.put("res", response);
            return render(model,"/hello");
        });
        get("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model,"/login");
        });

        post("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            List<String> lista = Arrays.asList(req.body().split("&"));
            String username = lista.get(0).substring(9);
            String password = lista.get(1).substring(9);
            String usernameUserDefault = "daniel";
            String passwordUserDefaultCrypt = encriptar("daniel");

            String resultado;
            req.session(true);
            req.session().attribute("Autenticado", false);
            if(username.equals(usernameUserDefault) && encriptar(password).equals(passwordUserDefaultCrypt)){
                resultado = "Usuario" + " " + username;
                req.session().attribute("Autenticado", true);
            }else{
                resultado = "Usuario o contraseña incorrecta";
            }
            model.put("result", resultado);


            return new ModelAndView(model,"/result");
        },new ThymeleafTemplateEngine());
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    private static String encriptar(String s) throws UnsupportedEncodingException{
            return Base64.getEncoder().encodeToString(s.getBytes("utf-8"));
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
