public class Instructors {
	String name;
	String surname;
	Boolean isPrimary;

	public Instructors(String name, String surname, Boolean isPrimary) {
		this.name = name;
		this.surname = surname;
		this.isPrimary = isPrimary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String toString() {
		return name + " " + surname + " " + isPrimary + " ";
	}

}
