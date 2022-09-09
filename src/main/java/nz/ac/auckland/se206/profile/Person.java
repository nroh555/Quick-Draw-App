package nz.ac.auckland.se206.profile;

import java.util.List;

public class Person {
	private String name;
	private Integer age;
	private Boolean isMarried;
	private List<String> hobbies;
	private List<Person> kids;

	public Person(String name, int age, boolean isMarried, List<String> hobbies,
			List<Person> kids) {
		this.name = name;
		this.age = age;
		this.isMarried = isMarried;
		this.hobbies = hobbies;
		this.kids = kids;
	}

	// getters and setters

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", age=" + age +
				", isMarried=" + isMarried +
				", hobbies=" + hobbies +
				", kids=" + kids +
				'}';
	}
}
