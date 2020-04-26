package multiplayer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MazeReq {

    public static void createMap(int size){
        try{
            String url = "http://localhost:8888/createMap?size=";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + size))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        }
        catch(IOException e) {
            System.exit(1);
        }
        catch(InterruptedException x){
            System.exit(1);

        }
    }



    public static int[][] getMap()  {
        try{
            String url = "http://localhost:8888/getMap";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

           String out = response.body();

           String[] temp = response.body().substring(2,out.length()-2).split("],\\[");

           int[][] coords = new int[temp.length][temp.length];

           for(int i = 0; i < coords.length; i++){
             String[] nums = temp[i].split(",");
             for(int j = 0; j < nums.length; j++){
                 coords[i][j] = Integer.parseInt(nums[j]);
//                 System.out.print(coords[i][j] + " ");
             }
//             System.out.println();
           }
           return coords;


        }
        catch(IOException e) {
            System.exit(1);
        }
        catch(InterruptedException x){
            System.exit(1);

        }
        return null;
    }
}
