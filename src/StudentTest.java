import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class StudentTest {
	Student student;
	CoursesOffered course;
	
	@Test
	public void testStudentObject(){
		student = new Student(null,null,null);
		assertNotNull(student);
	}
	
	@Test
	public void testStudentObject2(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student(null, "Id", course);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject3(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("Name", null, course);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObejct4(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("Name", "ID", null);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject5(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student(null, null, course);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject6(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("Name", null, null);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject7(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student(null, "ID", null);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject8(){	
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("", "ID", course);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject9(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("Name", "", course);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject10(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("", "", course);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStudentObject11(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("", "ID", null);
		assertEquals(expected, actual);
	}

	@Test
	public void testStudentObject12(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("Name", "", null);
		assertEquals(expected, actual);
	}


	@Test
	public void testStudentObject13(){
		Student expected = new Student("Name" ,  "Id", course);
		Student actual = new Student("", "", null);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetName(){
		student = new Student("", "Id", course);
		assertEquals("Name", student.getFullName());
	}
	
	@Test
	public void testGetName2(){
		
		student = new Student(null, "Id", course);
		assertEquals("Name", student.getFullName());
	}

	@Test
	public void testGetId(){
		
		student = new Student("Name", "", course);
		assertEquals("Id", student.getId());
	}

	@Test
	public void testID2(){
		
		student = new Student("Name", null , course);
		assertEquals("Id", student.getId());
	}
	
	@Test
	public void testGetCourse(){
		
		student = new Student("Name", "Id", null);
		assertEquals(course, student.getCourse());
	}
	
	
	@Test
	public void testToString(){
		
		student = new Student("Name", "Id", course);
		assertEquals("Id", student.toString());
	}
	
	

}
