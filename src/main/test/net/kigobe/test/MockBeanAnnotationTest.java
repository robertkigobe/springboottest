package net.kigobe.test;

import net.kigobe.MvcTestingExampleApplication;
import net.kigobe.component.dao.ApplicationDao;
import net.kigobe.component.models.CollegeStudent;
import net.kigobe.component.models.StudentGrades;
import net.kigobe.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes= MvcTestingExampleApplication.class)
public class MockBeanAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;
    @MockBean
    private ApplicationDao applicationDao;

    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach(){
        studentOne.setFirstname("Robert");
        studentOne.setLastname("Kigobe");
        studentOne.setEmailAddress("robertkigobe@gmail.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    public void assertEqualsTestAddGrades(){
        when(
                applicationDao.addGradeResultsForSingleClass(
                        studentGrades.getMathGradeResults())).thenReturn(100.00);

        assertEquals(100, applicationService.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults()
        ));

        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
        verify(applicationDao, times(1)).addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults());

    }

    @DisplayName("Find Gpa")
    @Test
    public void assertEqualsTestFindGpa(){
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults())).thenReturn(88.31);

        assertEquals(88.31, applicationService.findGradePointAverage(
                studentGrades.getMathGradeResults()
        ));

        verify(applicationDao).findGradePointAverage(studentGrades.getMathGradeResults());
    }

    @DisplayName("Not null")
    @Test
    public void testAssertNotNull(){
        when(applicationDao.checkNull(studentGrades.getMathGradeResults())).thenReturn(true);
        assertNotNull(applicationService.checkNull(studentOne.getStudentGrades().getMathGradeResults()), "Object should not be null");
    }

    @DisplayName("Throw runtime exception")
    @Test
    public void throwRunTimeException(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);
        assertThrows(RuntimeException.class, ()-> {
            applicationService.checkNull(nullStudent);
            verify(applicationDao, times(1)).checkNull(nullStudent);
        });
    }

    @DisplayName("Multiple stubbing")
    @Test
    public void stubbingConsecutiveTimes(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw exception second time");
        assertThrows(RuntimeException.class, ()-> {
            applicationService.checkNull(nullStudent);
        });
        assertEquals("Do not throw exception second time", applicationService.checkNull(nullStudent));
        verify(applicationDao, times(2)).checkNull(nullStudent);
    }
}
