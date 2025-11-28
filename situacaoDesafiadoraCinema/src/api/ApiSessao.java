package api;

import static spark.Spark.after;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import dao.SessaoDAO;
import model.Sessao;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

public class ApiSessao {
    // Instâncias
    private static final SessaoDAO dao = new SessaoDAO();
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                new JsonPrimitive(src.toString())
        )
        .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                LocalDateTime.parse(json.getAsString())
        )
        .create();

    private static final String APPLICATION_JSON = "application/json";

    public static void main(String[] args) {

        port(4567);

        // Sempre responder JSON
        after(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.type(APPLICATION_JSON);
            }
        });

        // -------------------------------
        // GET /sessoes (listar todas)
        // -------------------------------
        get("/sessoes", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                
                return gson.toJson(dao.buscarTodas());
            }
        });

        // -------------------------------
        // GET /sessoes/:id (buscar uma)
        // -------------------------------
        get("/sessoes/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    long id = Long.parseLong(request.params(":id"));
                    Sessao s = dao.buscarPorId(id);

                    if (s != null) {
                        return gson.toJson(s);
                    } else {
                        response.status(404);
                        return "{\"mensagem\": \"Sessão não encontrada\"}";
                    }
                } catch (Exception e) {
                    response.status(400);
                    return "{\"mensagem\": \"ID inválido\"}";
                }
            }
        });

        // -------------------------------
        // POST /sessoes (criar)
        // -------------------------------
        post("/sessoes", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Sessao nova = gson.fromJson(request.body(), Sessao.class);

                    dao.inserir(nova);

                    // Estado será atualizado automaticamente pelo DAO
                    nova.atualizarEstado();

                    response.status(201);
                    return gson.toJson(nova);
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\": \"Erro ao criar sessão\"}";
                }
            }
        });

        // -------------------------------
        // PUT /sessoes/:id (atualizar)
        // -------------------------------
        put("/sessoes/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    long id = Long.parseLong(request.params(":id"));
                    Sessao existente = dao.buscarPorId(id);

                    if (existente == null) {
                        response.status(404);
                        return "{\"mensagem\": \"Sessão não encontrada\"}";
                    }

                    Sessao atualizar = gson.fromJson(request.body(), Sessao.class);
                    atualizar.setId(id);

                    // Recalcular estado automático
                    atualizar.atualizarEstado();

                    dao.atualizar(atualizar);

                    return gson.toJson(atualizar);

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\": \"ID inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\": \"Erro ao atualizar sessão\"}";
                }
            }
        });

        // -------------------------------
        // DELETE /sessoes/:id
        // -------------------------------
        delete("/sessoes/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    long id = Long.parseLong(request.params(":id"));

                    if (dao.buscarPorId(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"Sessão não encontrada\"}";
                    }

                    dao.deletar(id);
                    response.status(204);
                    return "";

                } catch (Exception e) {
                    response.status(400);
                    return "{\"mensagem\": \"ID inválido\"}";
                }
            }
        });

    }
}
