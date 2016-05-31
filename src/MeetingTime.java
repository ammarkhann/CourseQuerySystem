public class MeetingTime {
	String startDate;
	String finishDate;
	String startHour;
	String finishHour;
	String day;
	String roomCode;

	public MeetingTime(String startDate, String finishDate, String startHour,
			String finishHour, String day, String roomCode) {
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.startHour = startHour;
		this.finishHour = finishHour;
		this.day = day;
		this.roomCode = roomCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getFinishHour() {
		return finishHour;
	}

	public void setFinishHour(String finishHour) {
		this.finishHour = finishHour;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String toString() {
		return startDate + " " + finishDate + " " + startHour + " "
				+ finishHour + " " + day + " " + roomCode + " ";
	}
}
