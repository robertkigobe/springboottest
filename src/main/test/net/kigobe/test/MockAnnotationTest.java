package net.kigobe.test;

import net.kigobe.MvcTestingExampleApplication;
import net.kigobe.component.dao.ApplicationDao;
import net.kigobe.component.models.CollegeStudent;
import net.kigobe.component.models.StudentGrades;
import net.kigobe.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest(classes= MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;
    @Mock
    private ApplicationDao applicationDao;

    @InjectMocks
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
}
