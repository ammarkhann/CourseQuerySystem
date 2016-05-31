import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoursesOffered {
	String courseNumber;
	String sectionNumber;
	String subjectName;
	List<Instructors> instructors;
	List<MeetingTime> meetingTimes = new LinkedList<MeetingTime>();
	List<Student> students = new LinkedList<Student>();

	public CoursesOffered(String subjectName, String courseNumber,
			String sectionNumber) {
		instructors = new LinkedList<Instructors>();
		this.subjectName = subjectName;
		this.courseNumber = courseNumber;
		this.sectionNumber = sectionNumber;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public List<MeetingTime> getMeetingTime() {
		return meetingTimes;

	}

	public void setMeetingTime(List<MeetingTime> meetingTimes) {
		this.meetingTimes = meetingTimes;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	void addInstructor(Instructors inst) {
		instructors.add(inst);
	}

	void addMeetingTime(MeetingTime meetingTime) {
		meetingTimes.add(meetingTime);
	}
	
	void addStudent(Student student){
		students.add(student);
	}

	void removeMeetingTime(MeetingTime meetingTime) {
		meetingTimes.remove(meetingTime);
	}

	public List<Instructors> getInstructors() {
		return instructors;
	}

	public void setInstructors(List<Instructors> instructors) {
		this.instructors = instructors;
	}

	public String toString() {
		return subjectName + " " + courseNumber + " " + sectionNumber + " ";
	}

}
