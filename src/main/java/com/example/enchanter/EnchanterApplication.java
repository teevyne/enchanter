package com.example.enchanter;

import com.twilio.rest.conversations.v1.conversation.Webhook;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Say;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.enchanter.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class EnchanterApplication implements CommandLineRunner {

	static Map<String, Object> gameState = new HashMap<>();
	private final TwilioService twilioService;
	private final GameService gameService;

	String voiceCommand;

	public static void main(String[] args) {
		SpringApplication.run(EnchanterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		// Getting to know the game player
		System.out.print("Hello. Enter your name: ");
		String name = scanner.nextLine();

		System.out.print("...and your phone number: ");
		String phoneNumber = scanner.nextLine();

		TimeUnit.SECONDS.sleep(2);

		System.out.println("Hello, " + name + "! Your number is: " + phoneNumber + ". " +
				"You are lost in the middle of the Enchanted Forest. Play this game to find your way home!");

		TimeUnit.SECONDS.sleep(2);

		System.out.println("Follow the prompts, use voice to interact with the game. Best of luck");

		TimeUnit.SECONDS.sleep(2);

		gameState.put("playerName", name);
		gameState.put("playerNumber", phoneNumber);
		gameState.put("playerScore", 0);
		gameState.put("playerLocation", "Deep in the hearts of the Enchanted Forest");
		gameState.put("playerInventory", new ArrayList<>());

		String toNumber = "aaa";
		String voiceCommandUrl = "www";
		twilioService.makeVoiceCall(toNumber, voiceCommandUrl);

		log.info("In here?");

		voiceCommand = twilioService.getVoiceResponse(Message.START_GAME);
		gameState.put("playerScore",
				(int) gameState.get("playerScore") +
						(int) gameService.makeGameMovementAndEarnPoint(voiceCommand).get("playerScore"));


		while (!gameService.isGameOver((Integer) gameState.get("playerScore"))) {

			voiceCommand = twilioService.getVoiceResponse(Message.NEXT_MOVE);
			gameState.put("playerScore",
					(int) gameState.get("playerScore") +
							(int) gameService.makeGameMovementAndEarnPoint(voiceCommand).get("playerScore"));

		}

		System.out.println("End of game! You have found your way out of the enchanted forest. Congratulations");


	}
}
