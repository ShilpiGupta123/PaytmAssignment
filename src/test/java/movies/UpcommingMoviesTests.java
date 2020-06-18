
package movies;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import io.restassured.response.Response;

public class UpcommingMoviesTests {

	Logger logger = Logger.getLogger(UpcommingMoviesTests.class);
	final static String ROOT_URI = "https://apiproxy.paytm.com/v2/movies/upcoming";

	/*
	 * Create tests for the following cases:-
	 *
	 * 1. Status code
	 * 2. Movie release date: should not be before today’s date
	 * 3. Movie Poster URL: should only have .jpg format
	 * 4. Paytm movie code: is unique
	 * 5. No movie code should have more than 1 language format
	 */
	@Test
	public static void testGetUpcomingMovies() {
		RestAssured.defaultParser = Parser.JSON;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		Response response = get(ROOT_URI);
		
		response.then().log().all();
		
		response.then().statusCode(200);
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
		
		response.then()
			.body("upcomingMovieData.moviePosterUrl", hasItems(endsWith(".jpg")))  
			.body("upcomingMovieData.language", hasItems(isA(String.class)))
			.body("upcomingMovieData.releaseDate", hasItems(greaterThan(LocalDate.now().format(dateTimeFormatter))));
			;

		 ArrayList<String> moviePosterUrl =  response.then().extract().path("upcomingMovieData.paytmMovieCode") ;
		 Set duplicates = findDuplicates(moviePosterUrl);
		 assertEquals(true, duplicates.isEmpty());
		 
		 writeToExel(response);
	}
	
	public static void writeToExel(Response response) {
		 String file = "/Users/shivam/Projects/java/PaytmMoviesTests/" + "Movies.xls";
		 List<Map<String, String>> movies = response.then().extract().response().jsonPath().getList("upcomingMovieData");
		 
		 try {
			WriteToExcelFile.writeMoviesNamWithIsContentAvailable0OnExel(file, movies);
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	}
	
	public static Set<String> findDuplicates(List<String> listContainingDuplicates) {
		 
		final Set<String> setToReturn = new HashSet<String>();
		final Set<String> set1 = new HashSet<String>();
 
		for (String yourInt : listContainingDuplicates) {
			if (!set1.add(yourInt)) {
				setToReturn.add(yourInt);
			}
		}
		return setToReturn;
	}
}