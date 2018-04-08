package cn.vaga.blazedemo;

import java.util.Date;
import java.util.Random;

public class RandNums {
	public int[] rand_nums = new int[1024];

	private int idx = 0;

	private Date date = new Date();

	public Random RAND = new Random(date.getDate() * date.getSeconds());

	public RandNums() {
		for (int i = 0; i < 1024; i++) {
			rand_nums[i] = RAND.nextInt(Integer.MAX_VALUE);
		}
	}

	public int nextInt() {
		if (idx >= 1024) {
			idx = 0;
		}
		return rand_nums[idx++];
	}
}
