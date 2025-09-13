import com.sun.net.httpserver.HttpServer;         // Para criar o servidor HTTP
import java.io.OutputStream;                       // Para enviar respostas ao navegador
import java.net.InetSocketAddress;                 // Para definir porta e endereço
import java.time.LocalTime;                        // Para pegar a hora atual
import java.time.format.DateTimeFormatter;         // Para formatar hora como HH:mm:ss

public class MeuServidor {

    public static void main(String[] args) throws Exception {

        // Cria servidor na porta 9000
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);

        // Lista de Tarefas no servidor
        java.util.List<Tarefa> tarefas = new java.util.ArrayList<>();
        tarefas.add(new Tarefa(1, "Estudar Java", false));
        tarefas.add(new Tarefa(2, "Treinar", true));
        tarefas.add(new Tarefa(3, "Ler um livro", false));

        // Lista de Comidas no Servidor
        java.util.List<Comida> comidas = new java.util.ArrayList<>();
        comidas.add(new Comida(4, "Maça", false));
        comidas.add(new Comida(5, "Salada", true));
        comidas.add(new Comida(6, "Banana", false));


        // =========================
        // Endpoint /hello
        // =========================
        server.createContext("/hello", exchange -> {
            String resposta = "Hello World!";  // mensagem de resposta

            // Envia cabeçalho HTTP (200 = OK) e tamanho da resposta
            exchange.sendResponseHeaders(200, resposta.getBytes().length);

            // Envia a mensagem de volta ao cliente
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        });

        // =========================
        // Endpoint /bye
        // =========================
        server.createContext("/bye", exchange -> {
            String resposta = "Tchau, Arthur";

            exchange.sendResponseHeaders(200, resposta.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        });

        // =========================
        // Endpoint /Time
        // =========================
        server.createContext("/Time", exchange -> {
            LocalTime agora = LocalTime.now();  // pega a hora no momento da requisição
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");  // formata hora:min:seg
            String resposta = "Agora são = " + agora.format(formatter);

            exchange.sendResponseHeaders(200, resposta.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        });
        // =========================
        // Endpoint / comida
        // =========================
        server.createContext("/comida", exchange -> {
            StringBuilder food = new StringBuilder("[");
            for(int i=0;i<comidas.size();i++){
                food.append(comidas.get(i).toJson());
                if(i!=comidas.size()-1){
                    food.append(", ");
                }
            }
            food.append("]");
            exchange.getResponseHeaders().set("Content-Type", "application/json");

            String resposta = food.toString();
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        });

        // =========================
        // Endpoint /ola?nome=SeuNome
        // =========================
        server.createContext("/ola", exchange -> {
            String query = exchange.getRequestURI().getQuery();  // pega "nome=Arthur"
            String nome = "amigo";  // valor padrão caso não passe o parâmetro

            // Se veio parâmetro "nome=..."
            if (query != null && query.contains("=")) {
                nome = query.split("=")[1];  // pega o valor depois do =
            }

            String resposta = "Olá, " + nome + "!";

            exchange.sendResponseHeaders(200, resposta.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        });

        // =========================
        // Endpoint / tarefa
        // =========================
        server.createContext("/tarefa", exchange -> {
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < tarefas.size(); i++) {//3
                json.append(tarefas.get(i).toJson());
                if (i < tarefas.size() - 1) json.append(","); // vírgula entre os itens
            }
            json.append("]");

            exchange.getResponseHeaders().set("Content-Type", "application/json");

            String resposta = json.toString();
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        });

        // =========================
        // Inicia o servidor
        // =========================
        server.start();

        // Mostra no console todos os endpoints disponíveis
        System.out.println("Servidor rodando em:");
        System.out.println("http://localhost:9000/hello");
        System.out.println("http://localhost:9000/bye");
        System.out.println("http://localhost:9000/Time");
        System.out.println("http://localhost:9000/ola?nome=Arthur");
        System.out.println("http://localhost:9000/tarefa");
        System.out.println("http://localhost:9000/comida");
    }
    static class Tarefa{
        int id;
        String titulo;
        boolean feito;

        public Tarefa(int id, String titulo, boolean feito) {
            this.id = id;
            this.titulo = titulo;
            this.feito = feito;
        }
        String toJson(){
            return "{ \"id\": " + id +
                    ", \"titulo\": \"" + titulo + "\"" +
                    ", \"feito\": " + feito + " }";
        }
    }
    static class Comida{
        int id;
        String titulo;
        boolean feito;

        public Comida(int id, String titulo, boolean feito) {
            this.id = id;
            this.titulo = titulo;
            this.feito = feito;
        }
        String toJson(){
            return "{ \"id\": " + id +
                    ", \"titulo\": \"" + titulo + "\"" +
                    ", \"feito\": " + feito + " }";
        }
    }
}
