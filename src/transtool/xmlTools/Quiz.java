/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transtool.xmlTools;

import java.util.ArrayList;
import transtool.questions.BrainhoneyContents;

/**
 * QUIZ Quiz holds a quiz name, questions, and the associated ID to each
 * question.
 *
 * Contents: Quiz Name - Contains the name of the quiz Quiz Questions - Contains
 * the ID of the quiz questions Brainhoney - Contains the actual quiz info
 *
 * @author hallm8
 *
 *
 */
public class Quiz {

    private String quizName;
    private ArrayList<String> quizQuestions;
    private ArrayList<BrainhoneyContents> brainhoney;
    private String quizID;
    
    public Quiz () {
        brainhoney = new ArrayList<>();
        quizQuestions = new ArrayList<>();
    }
    
    
    
    
    

    /**
     * QUIZ NAME
     *
     * Contains the Quiz Name
     *
     * @return Name of the quiz
     */
    public String getQuizName() {
        return quizName;
    }

    /**
     * QUIZ NAME
     *
     * Contains the quiz name
     *
     * @param quizName Name of the quiz
     */
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    /**
     * QUIZ QUESTIONS
     *
     * Contains the ID for all the quiz questions to link back to the items.
     *
     * @return Quiz question ID
     */
    public ArrayList<String> getQuizQuestions() {
        return quizQuestions;
    }

    /**
     * QUIZ QUESTIONS
     *
     * Contains the ID for all the quiz questions to link back to the items.
     *
     * @param quizQuestions Quiz question ID
     */
    public void setQuizQuestions(ArrayList<String> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    /**
     * Brainhoney
     *
     * Each Brainhoney class is a single quiz question.
     *
     * @return
     */
    public ArrayList<BrainhoneyContents> getBrainhoney() {
        return brainhoney;
    }

    /**
     * Brainhoney
     *
     * Each Brainhoney class is a single quiz question.
     *
     * @param brainhoney
     */
    public void setBrainhoney(ArrayList<BrainhoneyContents> brainhoney) {
        this.brainhoney = brainhoney;
    }

    /**
     * 
     * @return 
     */
    public String getQuizID() {
        return quizID;
    }

    /**
     * 
     * @param quizID 
     */
    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }
    
    
}
