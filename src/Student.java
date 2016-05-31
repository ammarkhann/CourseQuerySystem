
public class Student {

	String fullName;
	String id;
	CoursesOffered course;
	
	public Student(String fullName, String id, CoursesOffered course){
		this.fullName = fullName;
		this.id = id;
		this.course = course;
		
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CoursesOffered getCourse() {
		return course;
	}

	public void setCourse(CoursesOffered course) {
		this.course = course;
	}
	
	public String toString(){
		
		return fullName + " " + id + " " + course;
	}
	
	
}
