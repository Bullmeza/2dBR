package multiplayer;

//Robert Muresan
//2020/05/31
//2D Battle Royale
//Play with friends on a server/localhost


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MazeReq {
    private static String server = "http://localhost:8888";


    public String createMap(int size) {
        try {
            String url = server + "/createMap?size=";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + size))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException e) {
            System.out.println("SERVERS DOWN");
            System.exit(1);
        } catch (InterruptedException x) {
            System.out.println("SERVERS DOWN");
            System.exit(1);

        }
        return null;
    }


    public int[][] getMap() {
        try {
            String url = server + "/getMap";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String out = response.body();

            String[] temp = response.body().substring(2, out.length() - 2).split("],\\[");

            int[][] coords = new int[temp.length][temp.length];

            for (int i = 0; i < coords.length; i++) {
                String[] nums = temp[i].split(",");
                for (int j = 0; j < nums.length; j++) {
                    coords[i][j] = Integer.parseInt(nums[j]);
//                 System.out.print(coords[i][j] + " ");
                }
//             System.out.println();
            }
            return coords;


        } catch (IOException e) {
            System.out.println("SERVERS ARE DOWN");
            System.exit(1);
        } catch (InterruptedException x) {
            System.out.println("SERVERS ARE DOWN");
            System.exit(1);

        }
        return null;
    }

    public String[] playerWrite(int id, float x, float y, double angle) {
        try {
            String url = server + "/players?id=" + id + "&x=" + x + "&y=" + y + "&angle=" + angle;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());


            String out = response.body();
            out = out.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "");
            return out.split(",");

        } catch (IOException e) {
            System.out.println("SERVERS ARE DOWN");
            System.exit(1);
        } catch (InterruptedException e2) {
            System.out.println("SERVERS ARE DOWN");
            System.exit(1);
        }
        return null;
    }

    public static void bulletWrite(int player, float x, float y, double angle) {
        String url = server + "/bulletWrite?player=" + player + "&x=" + x + "&y=" + y + "&angle=" + angle;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);
    }

    public String[] bulletRead(int player) {
        try {
            String url = server + "/bulletRead?player=" + player;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            String out = response.body();
            System.out.println(out);
            if (out == "[]") {
                return null;
            }
//            System.out.print(out);
            out = out.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "");
            return out.split(",");
        } catch (IOException e) {
            System.out.println("SERVERS ARE DOWN");
            System.exit(1);
        } catch (InterruptedException e2) {
            System.out.println("SERVERS ARE DOWN");
            System.exit(1);
        }
        return null;
    }

    public static void playerKilled(int player_id) {
        String url = server + "/killed?player=" + player_id;


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);
    }
}
