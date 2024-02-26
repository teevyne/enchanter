package com.example.enchanter;

import org.springframework.stereotype.Service;
import com.example.enchanter.Message;
import java.util.Map;
import java.util.Random;

@Service
public class GameService {

    int movePoint;
    String message;

    private int generatePositiveScore() {
        Random random = new Random();
        return random.nextInt(41) - 10;
    }

    private String moveRemark(int movePoint) {
        if (movePoint <= 0) {
            return Message.WRONG_MOVE;
        } else {
            return Message.GOOD_MOVE;
        }
    }

    public Map<String, Object> makeGameMovementAndEarnPoint(String messagePrompt) throws Exception {

        switch (messagePrompt) {
            case "North":
                movePoint = generatePositiveScore();
                message = moveRemark(movePoint);
                break;
            case "South":
                movePoint = generatePositiveScore();
                message = moveRemark(movePoint);
                break;
            case "East":
                movePoint = generatePositiveScore();
                message = moveRemark(movePoint);
                break;
            case "West":
                movePoint = generatePositiveScore();
                message = moveRemark(movePoint);
                break;
            default:
                throw new Exception("You have passed in a wrong input");
        }

        return Map.of(
                "movePoint", movePoint,
                "message", message);
    }

    public boolean isGameOver(int playerScore) {

        if (playerScore <= 100) {return false;}
        return true;

    }
}
