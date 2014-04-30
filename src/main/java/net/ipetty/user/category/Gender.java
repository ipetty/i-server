package net.ipetty.user.category;

/**
 * 性别
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public enum Gender {

	None(0), Male(1), Female(2), Both(3);

	private static final int NONE_VALUE = 0;
	private static final int MALE_VALUE = 1;
	private static final int FEMALE_VALUE = 2;
	private static final int BOTH_VALUE = 3;

	private int value;

	private Gender() {
	}

	private Gender(int value) {
		this.value = value;
	}

	public static Gender valueOf(int value) {
		switch (value) {
		case NONE_VALUE:
			return None;
		case MALE_VALUE:
			return Male;
		case FEMALE_VALUE:
			return Female;
		case BOTH_VALUE:
			return Both;
		}
		return null;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
