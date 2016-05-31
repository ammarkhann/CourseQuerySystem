import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Date;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CourseQuerySystem {
	static List<CoursesOffered> allCourses = new LinkedList<CoursesOffered>();
	static List<String> courseNumbers = new ArrayList<String>();
	static List<String> instructorNames = new ArrayList<String>();
	static List<String> roomCodes = new ArrayList<String>();
	static List<String> subjectNames = new ArrayList<String>();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		initializeProgram();
	}

	public static void initializeProgram() throws FileNotFoundException {
		createCourseObject();
		getInstructorsArray();
		getScheduleArray();
		mainMenuInput();
	}

	public static void createCourseObject() throws FileNotFoundException {
		JSONArray courseData = getCourseDataFromJSON();
		for (int i = 0; i < courseData.size(); i++) {
			JSONObject course = (JSONObject) courseData.get(i);
			JSONObject courseObject = (JSONObject) course.get("Course");
			String subjectName = (String) courseObject.get("SubjectName");
			String courseNumber = (String) courseObject.get("CourseNo");
			String sectionNumber = (String) courseObject.get("SectionNo");
			CoursesOffered coursesOffered = new CoursesOffered(subjectName,
					courseNumber, sectionNumber);
			allCourses.add(coursesOffered);
		}
	}

	public static void getInstructorsArray() throws FileNotFoundException {
		JSONArray courseData = getCourseDataFromJSON();
		for (int index = 0; index < courseData.size(); index++) {
			JSONObject course = (JSONObject) courseData.get(index);
			JSONObject courseObject = (JSONObject) course.get("Course");
			JSONArray instructors = (JSONArray) courseObject.get("Instructors");
			if (instructors.size() == 0) {
				createEmptyInstructorsObject(index);
			} else {
				createInstructorsObject(index, instructors);
			}
		}
	}

	public static void createEmptyInstructorsObject(int index) {
		Instructors instructorObject = new Instructors("Empty", "Empty", null);
		CoursesOffered courses = allCourses.get(index);
		courses.addInstructor(instructorObject);
	}

	public static void createInstructorsObject(int courseIndex,
			JSONArray instructors) {
		for (int instructorIndex = 0; instructorIndex < instructors.size(); instructorIndex++) {
			JSONObject instructorsObject = (JSONObject) instructors
					.get(instructorIndex);
			String name = (String) instructorsObject.get("Name");
			String surname = (String) instructorsObject.get("Surname");
			Boolean isPrimary = (Boolean) instructorsObject.get("IsPrimary");
			Instructors instructorObject = new Instructors(name, surname,
					isPrimary);
			CoursesOffered courses = allCourses.get(courseIndex);
			courses.addInstructor(instructorObject);
		}
	}

	public static void getScheduleArray() throws FileNotFoundException {
		JSONArray courseData = getCourseDataFromJSON();
		for (int index = 0; index < courseData.size(); index++) {
			JSONObject course = (JSONObject) courseData.get(index);
			JSONObject courseObject = (JSONObject) course.get("Course");
			JSONArray schedule = (JSONArray) courseObject.get("Schedule");
			if (checkScheduleArraySize(schedule)) {
				createEmptyMeetingTimeObject(index);
			} else {
				createMeetingTimeObject(schedule, index);
			}
		}
	}

	public static JSONArray getCourseDataFromJSON()
			throws FileNotFoundException {
		FileReader reader = new FileReader("CoursesOffered.json");
		JSONArray courseData = (JSONArray) JSONValue.parse(reader);
		return courseData;
	}

	public static boolean checkScheduleArraySize(JSONArray schedule) {
		if (schedule.size() == 0) {
			return true;
		}
		return false;
	}

	public static void createEmptyMeetingTimeObject(int index) {
		MeetingTime meetingTime = new MeetingTime("Empty", "Empty", "Empty",
				"Empty", "Empty", "Empty");
		CoursesOffered courses = allCourses.get(index);
		courses.addMeetingTime(meetingTime);
	}

	public static void createMeetingTimeObject(JSONArray schedule, int index) {
		JSONObject scheduleObject = (JSONObject) schedule.get(0);
		String epochStartDate = (String) scheduleObject.get("StartDate");
		String epochFinishDate = (String) scheduleObject.get("FinishDate");
		String startDate = epochTimeConvert(epochStartDate);
		String finishDate = epochTimeConvert(epochFinishDate);
		JSONArray meetingTime = (JSONArray) scheduleObject.get("MeetingTime");
		for (int meetingTimeIndex = 0; meetingTimeIndex < meetingTime.size(); meetingTimeIndex++) {
			JSONObject meetingTimeObject = (JSONObject) meetingTime
					.get(meetingTimeIndex);
			String startHour = getStartHour(meetingTimeObject);
			String finishHour = getFinishHour(meetingTimeObject);
			String day = getDay(meetingTimeObject);
			JSONArray room = (JSONArray) meetingTimeObject.get("Room");
			if (room.size() == 0) {
				MeetingTime meetingTimes = new MeetingTime(startDate,
						finishDate, startHour, finishHour, day, "Empty");
				CoursesOffered courses = allCourses.get(index);
				courses.addMeetingTime(meetingTimes);
			} else {
				for (int roomIndex = 0; roomIndex < room.size(); roomIndex++) {
					JSONObject roomObject = (JSONObject) room.get(roomIndex);
					String roomCode = (String) roomObject.get("RoomCode");
					MeetingTime meetingTimes = new MeetingTime(startDate,
							finishDate, startHour, finishHour, day, roomCode);
					CoursesOffered courses = allCourses.get(index);
					courses.addMeetingTime(meetingTimes);
				}
			}
		}
	}

	public static String epochTimeConvert(String epochDate) {
		int index = epochDate.indexOf('(');
		int endIndex = epochDate.indexOf(')');
		Long epoch = Long.parseLong(epochDate.substring(index + 1, endIndex));
		Date date = new Date(epoch);
		DateFormat format = new SimpleDateFormat("EEEE d MMM yyyy HH:mm:ss z ",
				Locale.ENGLISH);
		format.setTimeZone(TimeZone.getDefault());
		String formattedDate = format.format(date);
		return formattedDate;
	}

	public static String getStartHour(JSONObject meetingTimeObject) {
		return (String) meetingTimeObject.get("StartHour");
	}

	public static String getFinishHour(JSONObject meetingTimeObject) {
		return (String) meetingTimeObject.get("FinishHour");
	}

	public static String getDay(JSONObject meetingTimeObject) {
		return (String) meetingTimeObject.get("Day");
	}

	public static void printMainMenu() {
		System.out.println("\nWelcome to CodeHero Academy");
		System.out.println("\nYou can choose from the following menu");
		System.out.println("\n1)Get to list any one of the following: \n");
		System.out.println("\t- instructors");
		System.out.println("\t- rooms");
		System.out.println("\t- subject names");
		System.out.println("\t- course numbers");
		System.out
				.println(("\n2)Get specifc information about the courses:\n"));
		System.out.println("\t- Room");
		System.out.println("\t- Day");
		System.out.println("\t- Instructor");
		System.out.println("\t- Course Number");
		System.out.println("\t- Subject Name");
		System.out
				.println("\t- Courses that start in the morning (starting time range 8:40-11:40)\n");
		System.out.println("3)Add a new course\n");
		System.out.println("4)Add a new student\n");
		System.out.println("5)List weekly plan of the student\n");

		System.out.println("6)Exit\n");
	}

	public static void mainMenuInput() {
		printMainMenu();
		System.out.print(">>>>");
		String input = scanner.next();
		if (input.equals("1")) {
			printListingMenu();
		} else if (input.equals("2")) {
			printSpecificInfoMenu();
		} else if (input.equals("3")) {
			addNewCourse();
		} else if (input.equals("4")) {
			createNewStudent();
		} else if (input.equals("5")) {
			getWeeklyPlan();
		} else if (input.equals("6")) {
			System.out.println("\nGood Bye!");
			System.exit(0);
		} else {
			printMainMenuError();
			mainMenuInput();
		}
	}

	public static void printListingMenu() {
		System.out.println("\nSelect anyone of the following to list:");
		System.out.println("\t1) instructors");
		System.out.println("\t2) rooms");
		System.out.println("\t3) subject names");
		System.out.println("\t4) course numbers");
		System.out.println("\t5) Back to Main Menu");
		listingMenuInput();
	}

	public static void printSpecificInfoMenu() {
		System.out
				.println("\nQuery a course by selecting any one of the following options:");
		System.out.println("\t1) Room");
		System.out.println("\t2) Day");
		System.out.println("\t3) Instructor");
		System.out.println("\t4) Course Number");
		System.out.println("\t5) Subject Name");
		System.out
				.println("\t6) Courses that start in the morning (starting time range 8:40-11:40)");
		System.out.println("\t7) Back to Main Menu");
		specificMenuInput();
	}

	public static void printMainMenuError() {
		System.out.print("|------------------------------------------------|");
		System.out.println("\n|\tThe choice you made is invalid.          |");
		System.out.println("|\tRemember the choices are either 1,2 or 3 |");
		System.out
				.println("|------------------------------------------------|");
	}

	public static void listingMenuInput() {
		System.out.print(">>>");
		String userInput = scanner.next();
		if (userInput.equals("1")) {
			listInstructors();
			printListingMenu();
		} else if (userInput.equals("2")) {
			listRooms();
			printListingMenu();
		} else if (userInput.equals("3")) {
			listSubjectNames();
			printListingMenu();
		} else if (userInput.equals("4")) {
			listCourseNumbers();
			printListingMenu();
		} else if (userInput.equals("5")) {
			mainMenuInput();
		} else {
			printListingMenuError();
			listingMenuInput();
		}
	}

	public static void listInstructors() {
		populateInstructorNamesList();
		String temporaryName = " ";
		Collections.sort(instructorNames,
				Collator.getInstance(Locale.forLanguageTag("tr_TR")));
		for (int index = 0; index < instructorNames.size(); index++) {
			if (!(instructorNames.get(index).equals(temporaryName))) {
				System.out.println(instructorNames.get(index));
			}
			temporaryName = instructorNames.get(index);
		}
		printDottedLine();
	}

	public static void populateInstructorNamesList() {
		for (int courseIndex = 0; courseIndex < allCourses.size(); courseIndex++) {
			List<Instructors> instructors = allCourses.get(courseIndex).instructors;
			for (int instructorIndex = 0; instructorIndex < instructors.size(); instructorIndex++) {
				instructorNames.add(allCourses.get(courseIndex).instructors
						.get(instructorIndex).name
						+ " "
						+ allCourses.get(courseIndex).instructors
								.get(instructorIndex).surname);
			}
		}
	}

	public static void listRooms() {
		populateRoomCodesList();
		Collections.sort(roomCodes);
		String temporaryRoomCode = " ";
		for (int index = 0; index < roomCodes.size(); index++) {
			if (!(roomCodes.get(index).equals(temporaryRoomCode))
					&& !(roomCodes.get(index).equals("Empty"))) {
				System.out.println(roomCodes.get(index));
			}
			temporaryRoomCode = roomCodes.get(index);
		}
		printDottedLine();
	}

	public static void populateRoomCodesList() {
		for (int courseIndex = 0; courseIndex < allCourses.size(); courseIndex++) {
			List<MeetingTime> meetingTimes = allCourses.get(courseIndex).meetingTimes;
			for (int meetingTimesIndex = 0; meetingTimesIndex < meetingTimes
					.size(); meetingTimesIndex++) {
				roomCodes.add(allCourses.get(courseIndex).meetingTimes
						.get(meetingTimesIndex).roomCode);
			}
		}
	}

	public static void listSubjectNames() {
		populateSubjectNamesList();
		Collections.sort(subjectNames);
		String temporaryName = " ";
		for (int index = 0; index < subjectNames.size(); index++) {
			if (!(subjectNames.get(index).equals(temporaryName))) {
				System.out.println(subjectNames.get(index));
			}
			temporaryName = subjectNames.get(index);
		}
		printDottedLine();
	}

	public static void populateSubjectNamesList() {
		for (int index = 0; index < allCourses.size(); index++) {
			subjectNames.add(allCourses.get(index).subjectName);
		}
	}

	public static void listCourseNumbers() {
		populateCourseNumbersList();
		Collections.sort(courseNumbers);
		String temporaryName = " ";
		for (int index = 0; index < courseNumbers.size(); index++) {
			if (!(courseNumbers.get(index).equals(temporaryName))) {
				System.out.println(courseNumbers.get(index));
			}
			temporaryName = courseNumbers.get(index);
		}
		printDottedLine();
	}

	public static void populateCourseNumbersList() {
		for (int index = 0; index < allCourses.size(); index++) {
			courseNumbers.add(allCourses.get(index).courseNumber);
		}
	}

	public static void printListingMenuError() {
		System.out.print("|------------------------------------------------|");
		System.out.println("\n|\tThe choice you made is invalid.          |");
		System.out.println("|\tRemember the choices are 1,2,3,4 and 5   |");
		System.out
				.println("|------------------------------------------------|");
	}

	public static void specificMenuInput() {
		System.out.print(">>>>");
		String userInput = scanner.next();
		if (userInput.equals("1")) {
			coursesByRoom();
		} else if (userInput.equals("2")) {
			coursesByDay();
		} else if (userInput.equals("3")) {
			coursesByInstructor();
		} else if (userInput.equals("4")) {
			coursesByCourseNumber();
		} else if (userInput.equals("5")) {
			coursesBySubjectName();
		} else if (userInput.equals("6")) {
			coursesByMorningTime();
		} else if (userInput.equals("7")) {
			mainMenuInput();
		} else {
			printSpecificMenuError();
			printSpecificInfoMenu();
		}
	}

	public static void coursesByRoom() {
		System.out.print("\nEnter Room Number:");
		String userInput = scanner.next();
		if (!checkRoomNumberInput(userInput)) {
			printInvalidRoomError();
			coursesByRoom();
		}
		for (int index = 0; index < allCourses.size(); index++) {
			int meetingTimesIndex = 0;
			String roomCode = allCourses.get(index).meetingTimes
					.get(meetingTimesIndex).roomCode;
			if (userInput.equals(roomCode.replaceAll("\\s+", ""))) {
				System.out.println();
				getSpecificCourseInfo(index);
				getSpecificMeetingTimes(index);
				getSpecificInstructors(index);
				printDottedLine();
			}
		}
		printSpecificInfoMenu();
	}

	public static boolean checkRoomNumberInput(String userInput) {
		for (int courseIndex = 0; courseIndex < allCourses.size(); courseIndex++) {
			List<MeetingTime> meetingTimes = allCourses.get(courseIndex).meetingTimes;
			for (int meetingTimesIndex = 0; meetingTimesIndex < meetingTimes
					.size(); meetingTimesIndex++) {
				String roomCode = allCourses.get(courseIndex).meetingTimes
						.get(meetingTimesIndex).roomCode;
				if (userInput.toUpperCase().equals(
						roomCode.replaceAll("\\s+", ""))) {
					return true;
				}
			}
		}
		return false;
	}

	public static void printInvalidRoomError() {
		System.out.println("\nThe room number you entered does not exist.");
		System.out.println("Make sure the format you are entering is correct");
		System.out.println("For e.g CK:FEAS:G16 ");
	}

	public static void coursesByDay() {
		System.out.print("\nEnter Day:");
		String userInput = scanner.next();
		if (!checkDayInput(userInput)) {
			printInvalidDayError();
			coursesByDay();
		}
		for (int courseIndex = 0; courseIndex < allCourses.size(); courseIndex++) {
			List<MeetingTime> meetingTimes = allCourses.get(courseIndex).meetingTimes;
			for (int meetingTimesIndex = 0; meetingTimesIndex < meetingTimes
					.size(); meetingTimesIndex++) {
				if (userInput
						.equalsIgnoreCase(allCourses.get(courseIndex).meetingTimes
								.get(meetingTimesIndex).day)) {
					getSpecificCourseInfo(courseIndex);
					getSpecificMeetingTimes(courseIndex);
					getSpecificInstructors(courseIndex);
					printDottedLine();
				}
			}
		}
		printSpecificInfoMenu();
	}

	public static boolean checkDayInput(String userInput) {
		for (int courseIndex = 0; courseIndex < allCourses.size(); courseIndex++) {
			List<MeetingTime> meetingTimes = allCourses.get(courseIndex).meetingTimes;
			for (int meetingTimesIndex = 0; meetingTimesIndex < meetingTimes
					.size(); meetingTimesIndex++) {
				if (userInput
						.equalsIgnoreCase(allCourses.get(courseIndex).meetingTimes
								.get(meetingTimesIndex).day)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void printInvalidDayError() {
		System.out.println("\nThe day you entered does not exist.");
		System.out.println("Try entering again\n");
	}

	public static void coursesByInstructor() {
		System.out.print("\nEnter Instructors First Name:");
		Scanner newScanner = new Scanner(System.in); // The static defined
														// scanner is causing
														// problem // problem
														// here
		String userInput = newScanner.nextLine(); // You can test it.It skips
													// line and I tried all
													// solutions
		if (!checkInstructorInput(userInput)) {
			printInvalidInstructorError();
			coursesByInstructor();
		}
		for (int courseIndex = 0; courseIndex < allCourses.size(); courseIndex++) {
			List<Instructors> instructors = allCourses.get(courseIndex).instructors;
			for (int instructorIndex = 0; instructorIndex < instructors.size(); instructorIndex++) {
				String a = allCourses.get(courseIndex).instructors
						.get(instructorIndex).name;
				if (userInput.toUpperCase(new Locale("tr", "TR")).equals(
						allCourses.get(courseIndex).instructors
								.get(instructorIndex).name)) {
					getSpecificCourseInfo(courseIndex);
					getSpecificMeetingTimes(courseIndex);
					getSpecificInstructors(courseIndex);
					printDottedLine();
				}
			}

		}
		printSpecificInfoMenu();
	}

	public static boolean checkInstructorInput(String userInput) {
		for (int courseIndex = 0; courseIndex < allCourses.size(); courseIndex++) {
			List<Instructors> instructors = allCourses.get(courseIndex).instructors;
			for (int instructorIndex = 0; instructorIndex < instructors.size(); instructorIndex++) {
				String a = allCourses.get(courseIndex).instructors
						.get(instructorIndex).name;
				if (userInput.toUpperCase(new Locale("tr", "TR")).equals(
						allCourses.get(courseIndex).instructors
								.get(instructorIndex).name)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void printInvalidInstructorError() {
		System.out.println("\nThe instructor name you entered does not exist.");
		System.out.println("Try entering again\n");
	}

	public static void coursesByCourseNumber() {
		System.out.println("\nEnter Course Number:");
		String userInput = scanner.next();
		if (!checkCourseNumberInput(userInput)) {
			printInvalidCourseNumberError();
			coursesByCourseNumber();
		}
		for (int index = 0; index < allCourses.size(); index++) {
			if (userInput.equals(allCourses.get(index).courseNumber)) {
				getSpecificCourseInfo(index);
				getSpecificMeetingTimes(index);
				getSpecificInstructors(index);
				printDottedLine();
			}
		}
		printSpecificInfoMenu();
	}

	public static boolean checkCourseNumberInput(String userInput) {
		for (int index = 0; index < allCourses.size(); index++) {
			if (userInput.equals(allCourses.get(index).courseNumber)) {
				return true;
			}
		}
		return false;
	}

	public static void printInvalidCourseNumberError() {
		System.out.println("\nThe course number you entered does not exist.");
		System.out.println("Try entering again\n");
	}

	public static void coursesBySubjectName() {
		System.out.print("\nEnter Subject Name:");
		String userInput = scanner.next();
		if (!checkSubjectNameInput(userInput)) {
			printInvalidSubjectNameError();
			coursesBySubjectName();
		}
		for (int index = 0; index < allCourses.size(); index++) {
			if (userInput.toUpperCase().equals(
					allCourses.get(index).subjectName)) {
				getSpecificCourseInfo(index);
				getSpecificMeetingTimes(index);
				getSpecificInstructors(index);
				printDottedLine();
			}
		}
		printSpecificInfoMenu();

	}

	public static boolean checkSubjectNameInput(String userInput) {
		for (int index = 0; index < allCourses.size(); index++) {
			if ((userInput.toUpperCase()
					.equals(allCourses.get(index).subjectName)))
				return true;
		}
		return false;
	}

	public static void printInvalidSubjectNameError() {
		System.out.println("\nThe subject name you entered does not exist.");
		System.out.println("Try entering again\n");
	}

	public static void coursesByMorningTime() {
		System.out.print("\nEnter Time:");
		String userInput = scanner.next();
		if (!checkMorningTimeInput(userInput)) {
			printInvalidTimeError();
			coursesByMorningTime();
		}
		for (int index = 0; index < allCourses.size(); index++) {
			String startHour = allCourses.get(index).meetingTimes.get(0).startHour;
			if (userInput.equals(startHour.replaceAll("\\s+", ""))) {
				getSpecificCourseInfo(index);
				getSpecificMeetingTimes(index);
				getSpecificInstructors(index);
				printDottedLine();
			}
		}
		printSpecificInfoMenu();
	}

	public static boolean checkMorningTimeInput(String userInput) {
		if (userInput.equals("8:40") || userInput.equals("9:40")
				|| userInput.equals("10:40") || userInput.equals("11:40")) {
			return true;
		}
		return false;
	}

	public static void printInvalidTimeError() {
		System.out.println("\nThe time you entered is invalid.");
		System.out.println("Try entering again\n");
	}

	public static void printSpecificMenuError() {
		System.out
				.print("|--------------------------------------------------|");
		System.out.println("\n|\tThe choice you made is invalid.            |");
		System.out.println("|\tRemember the choices are 1,2,3,4,5,6 and 7 |");
		System.out
				.println("|--------------------------------------------------|");
	}

	public static void getSpecificCourseInfo(int index) {
		System.out.println("Course\t[");
		System.out.println("Subject Name:" + allCourses.get(index).subjectName);
		System.out.println("Course Number:"
				+ allCourses.get(index).courseNumber);
		System.out.println("Section Number:"
				+ allCourses.get(index).sectionNumber);
	}

	public static void getSpecificMeetingTimes(int courseIndex) {
		List<MeetingTime> meetingTimes = allCourses.get(courseIndex).meetingTimes;
		for (int meetingTimesIndex = 0; meetingTimesIndex < meetingTimes.size(); meetingTimesIndex++) {
			if (meetingTimesIndex == 0) {
				System.out.println("Schedule= [Schedule:");
				System.out.println("StartDate = "
						+ allCourses.get(courseIndex).meetingTimes
								.get(meetingTimesIndex).startDate);
				System.out.println("FinishDate = "
						+ allCourses.get(courseIndex).meetingTimes
								.get(meetingTimesIndex).finishDate);
				System.out.print("MeetingTime = ");
			}
			System.out.print("[Meeting Time: Day = "
					+ allCourses.get(courseIndex).meetingTimes
							.get(meetingTimesIndex).day
					+ ", StartHour = "
					+ allCourses.get(courseIndex).meetingTimes
							.get(meetingTimesIndex).startHour
					+ ", FinishHour = "
					+ allCourses.get(courseIndex).meetingTimes
							.get(meetingTimesIndex).finishHour
					+ ",Rooms =[Room [ RoomCode = "
					+ allCourses.get(courseIndex).meetingTimes
							.get(meetingTimesIndex).roomCode + " ]]");
			if (meetingTimesIndex != meetingTimes.size() - 1) {
				System.out.print(", ");
			}
			if (meetingTimesIndex == meetingTimes.size() - 1) {
				System.out.println("]]");
			}
		}
	}

	public static void getSpecificInstructors(int courseIndex) {
		List<Instructors> instructors = allCourses.get(courseIndex).instructors;
		for (int meetingTimesIndex = 0; meetingTimesIndex < instructors.size(); meetingTimesIndex++) {
			System.out.println("Instructors=[Name = "
					+ allCourses.get(courseIndex).instructors
							.get(meetingTimesIndex).name
					+ ", Surname = "
					+ allCourses.get(courseIndex).instructors
							.get(meetingTimesIndex).surname
					+ ", IsPrimary = "
					+ allCourses.get(courseIndex).instructors
							.get(meetingTimesIndex).isPrimary + "]");
		}

		System.out.println("\t]");
	}

	public static void addNewCourse() {

		addNewCourseObject();

	}

	public static void addNewCourseObject() {
		System.out.println("Enter Subject Name:");
		String subjectName = scanner.next();
		System.out.println("Enter Course Number:");
		String courseNumber = scanner.next();
		System.out.println("Enter Section Number:");
		String sectionNumber = scanner.next();
		CoursesOffered course = new CoursesOffered(subjectName, courseNumber,
				sectionNumber);
		allCourses.add(course);
		addNewInstructorObject(course);
	}

	public static void addNewInstructorObject(CoursesOffered course) {
		System.out.println("Enter Instructors Name:");
		String name = scanner.next();
		System.out.println("Enter Instructors Surname:");
		String surname = scanner.next();
		Boolean isPrimary = getIsPrimaryStatus();
		Instructors instructor = new Instructors(name, surname, isPrimary);
		course.addInstructor(instructor);
		multipleInstructorSelectionMenu(course);
	}

	public static Boolean getIsPrimaryStatus() {
		System.out
				.println("Enter Primary Status of Instructor(True or False):");
		try {
			Boolean isPrimary = scanner.nextBoolean();
			return isPrimary;
		} catch (Exception e) {
			System.out.println("Invalid Entry");
			System.out.println("The value should be either True or False");
			scanner.next();
			getIsPrimaryStatus();
		}
		return null;
	}

	public static void multipleInstructorSelectionMenu(CoursesOffered course) {
		System.out
				.println("Do you want to add another instructor for this course");
		System.out.println("1)Yes");
		System.out.println("2)No");
		String userInput = scanner.next();
		if (userInput.equals("1")) {
			addNewInstructorObject(course);
		} else if (userInput.equals("2")) {
			addNewMeetingTimeObject(course);
		} else {
			System.out.println("Invalid Choice Selection!");
		}
	}

	public static void addNewMeetingTimeObject(CoursesOffered course) {
		String day = getDay();
		String startHour = getStartHour();
		String finishHour = getFinishHour();
		String roomCode = getRoomCode();
		String startDate = getStartDate();
		String finishDate = getFinishDate();
		MeetingTime meetingTime = new MeetingTime(startDate, finishDate,
				startHour, finishHour, day, roomCode);
		course.addMeetingTime(meetingTime);
		multipleMeetingTimeSelectionMenu(course);
		mainMenuInput();
	}

	public static String getDay() {
		System.out.println("Enter the day:");
		String day = scanner.next();
		return day;
	}

	public static String getStartHour() {
		System.out.println("Enter Start Hour:");
		scanner.nextLine();
		String startHour = scanner.nextLine();
		return startHour;
	}

	public static String getFinishHour() {
		System.out.println("Enter Finish Hour:");
		String finishHour = scanner.nextLine();
		return finishHour;
	}

	public static String getRoomCode() {
		System.out.println("Enter the room code for the room");
		String roomCode = scanner.nextLine();
		return roomCode;
	}

	public static String getStartDate() {
		String formattedStartDate = "";
		try {
			System.out
					.println("Enter Start Date in the format (Day/Month/Year Hour:Minute:Second AM/PM):");
			String startDate = scanner.nextLine();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy hh:mm:ss a");
			formattedStartDate = simpleDateFormat.parse(startDate).toString();
		} catch (Exception e) {
			System.out
					.println("Date entered in an invalid format. Try entering again.");
			getStartDate();
		}
		return formattedStartDate;
	}

	public static String getFinishDate() {
		String formattedStartDate = "";
		try {
			System.out
					.println("Enter Finish Date in the format (Day/Month/Year Hour:Minute:Second AM/PM):");
			String finishDate = scanner.nextLine();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy hh:mm:ss a");
			formattedStartDate = simpleDateFormat.parse(finishDate).toString();
		} catch (Exception e) {
			System.out
					.println("Date entered in an invalid format. Try entering again.");
			getStartDate();
		}
		return formattedStartDate;
	}

	public static void multipleMeetingTimeSelectionMenu(CoursesOffered course) {
		System.out.println("Do you want to another meeting time?");
		System.out.println("1)Yes");
		System.out.println("2)No");
		String userInput = scanner.next();
		if (userInput.equals("1")) {
			addNewMeetingTimeObject(course);
		} else if (userInput.equals("2")) {
			return;
		} else {
			System.out.println("Invlaid Choice Selection!");
		}
	}

	public static void createNewStudent() {
		scanner.nextLine();
		String fullName = getStudentName();
		String id = getStudentId();
		CoursesOffered course = getCourseOffered();
		if (course != null) {
			Student student = new Student(fullName, "S00-" + id, course);
			course.addStudent(student);
			addMultipleCourses(fullName, id);
		} else if (course == null) {
			System.out
					.println("Invalid section number , course number or subject name.Try entering again");
			createNewStudent();
		}

	}

	public static String getStudentName() {
		System.out.println("Enter student's full name:");
		String fullName = scanner.nextLine();
		return fullName;
	}

	public static String getStudentId() {
		System.out.println("Enter student's ID:");
		System.out.print("S00-");
		String id = scanner.next();
		return id;
	}

	public static CoursesOffered getCourseOffered() {
		System.out.println("Enter courses taken by the student:");
		System.out.println(">>>Enter subject name):");
		CoursesOffered course = null;
		String name = scanner.next();
		System.out.println(">>>Enter section number:");
		String sectionNo = scanner.next();
		System.out.println(">>>Enter course number:");
		String courseNo = scanner.next();
		for (int i = 0; i < allCourses.size(); i++) {
			if (allCourses.get(i).subjectName.equals(name.toUpperCase())
					&& allCourses.get(i).sectionNumber.equals(sectionNo
							.toUpperCase())
					&& allCourses.get(i).courseNumber.equals(courseNo)) {
				course = allCourses.get(i);
			}
		}
		return course;
	}

	public static void addMultipleCourses(String fullName, String id) {
		printMultipleCourseMenu();
		String userInput = scanner.next();
		if (userInput.equals("1")) {
			CoursesOffered course = getCourseOffered();
			Student student = new Student(fullName, "S00-" + id, course);
			course.addStudent(student);
			addMultipleCourses(fullName, id);
		} else {
			mainMenuInput();
		}
	}

	public static void printMultipleCourseMenu() {
		System.out
				.println("Do you want to enter another course for this student?");
		System.out.println("1)Yes");
		System.out.println("2)No");
	}

	public static void getWeeklyPlan() {
		System.out.println("Enter students ID:");
		System.out.print("S00-");
		scanner.nextLine();
		String userInput = scanner.nextLine();
		printWeeklyPlanData(userInput);
		mainMenuInput();

	}

	public static void printWeeklyPlanData(String userInput) {
		for (int i = 0; i < allCourses.size(); i++) {
			List<Student> student = allCourses.get(i).students;
			for (int j = 0; j < student.size(); j++) {
				if (student.get(j).id.equals("S00-" + userInput)) {
					System.out.println(student.get(j).fullName);
					System.out.println(student.get(j).id);
					System.out.println(student.get(j).course);
					getSpecificMeetingTimes(i);
					getSpecificInstructors(i);
					printDottedLine();
				}
			}
		}
	}

	public static void printDottedLine() {
		System.out
				.println("---------- ---------- ---------- ---------- ---------- ---------- "
						+ "----------- ---------- ------------ ----------- ----------- -----");
	}
}
