package cc.mrbird.febs.manager.entity;

import java.util.ArrayList;
import java.util.List;

import cc.mrbird.febs.common.utils.JsonUtils;
import lombok.Data;
@Data
public class ScoreModel {
	private List<Score>scores;
	public static void main(String[] args) {
		ScoreModel scoreModel = new ScoreModel();
		Score score = new Score();
		score.setScore(98.0);
		score.setUserId(4L);
		List<Score>scores = new ArrayList<>();
		scores.add(score);
		scoreModel.setScores(scores);
		System.out.println(JsonUtils.objectToJson(scoreModel));
	}
}
