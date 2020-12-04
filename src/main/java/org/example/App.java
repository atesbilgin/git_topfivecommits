/**
 * This application gets the top 10 contributors of the Facebooks react repository, and prints them to console
 * @author  Ateş Bilgin
 */
package org.example;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class


public class App 
{

    /**
     * This method gets top 10 contributor names and contribution counts to Facebooks react repository.
     * @return ArrayList of Contributors.
     */
    public static ArrayList<Contributor> getTop10ReactContributors() throws IOException, InterruptedException
    {

        //generate HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        //send GET request to github api, save it to HttpResponse instance
        String url = "https://api.github.com/repos/facebook/react/contributors?anon=true";
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Parse JSON into contributor list
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  //skip missing variables
        List<Contributor> contributors = mapper.readValue(response.body(), new TypeReference<List<Contributor>>() {});

        //save top 10 contributors to ArrayList and return
        ArrayList<Contributor> top10Contributors = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            top10Contributors.add(contributors.get(i));
        }

        return top10Contributors;
    }

    /**
     * This method gets top 10 contributor names and contribution counts to Facebooks react repository.
     * @param username Username of the contributor
     * @return Contributor Returns the contributor with name, location, and company features.
     */
    public static Contributor getUserInfo(String username) throws IOException, InterruptedException {

        //generate HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        //generate url and send GET request to github api
        String url = "https://api.github.com/users/" + username;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();

        //save results to HttpResponse instance, result is already sorted by contribution counts in descending order
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Parse JSON into contributor object and return
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Contributor contributor = mapper.readValue(response.body(), new TypeReference<Contributor>(){});

        return contributor;
    }



    public static void main( String[] args ) throws IOException, InterruptedException {

        System.out.println("repo:facebook/react");
        ArrayList<Contributor> contributors = getTop10ReactContributors();  // get names and contribution counts of top 10 users

        //get location and company info, add to contributors ArrayList
        for(int i = 0; i < 10; i++)
        {
            Contributor contributor = getUserInfo(contributors.get(i).getLogin());
            contributor.setContributions(contributors.get(i).getContributions());
            contributors.set(i,contributor);
        }

        //Print results to console
        contributors.forEach(System.out::println);



        //open output.txt file
        try {
            File myObj = new File("output.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //write contributor info to the file
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write("repo:facebook/react \n");

            for(int i = 0; i < 10; i++)
            {
                myWriter.write(contributors.get(i).toString() + "\n");

            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
