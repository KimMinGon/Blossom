package com.exam.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface BlossomDAO {
	public ArrayList<BlossomTO> userList();
	public int[] userDatas();
	public int[] characterDatas();
	public int[] characterPoints();
	public int[] walkingDatas();
	public int[] drinkingDatas();
	public ArrayList<BlossomTO> gpsDistance();

}
