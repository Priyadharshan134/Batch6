package com.example.opinionpoll;

public class ResultHelper {
    String Question, Option1, Option2, Option3, Option4, QuestionId, OwnerMailID, OwnerUserID, CreationDate,CreationTime;
    int OptionNum;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOption1() {
        return Option1;
    }

    public void setOption1(String option1) {
        Option1 = option1;
    }

    public String getOption2() {
        return Option2;
    }

    public void setOption2(String option2) {
        Option2 = option2;
    }

    public String getOption3() { return Option3; }

    public void setOption3(String option3) { Option3 = option3; }

    public String getOption4() {
        return Option4;
    }

    public void setOption4(String option4) {
        Option4 = option4;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public String getOwnerMailID() {
        return OwnerMailID;
    }

    public void setOwnerMailID(String ownerMailID) {
        OwnerMailID = ownerMailID;
    }

    public String getOwnerUserID() {
        return OwnerUserID;
    }

    public void setOwnerUserID(String ownerUserID) {
        OwnerUserID = ownerUserID;
    }

    public int getOptionNum() {
        return OptionNum;
    }

    public void setOptionNum(int optionNum) {
        OptionNum = optionNum;
    }

    public ResultHelper(String question, String option1, String option2, String questionId, String creationDate, String creationTime, String ownerMailID, String ownerUserID, int optionNum) {
        Question = question;
        Option1 = option1;
        Option2 = option2;
        QuestionId = questionId;
        CreationDate = creationDate;
        CreationTime = creationTime;
        OwnerMailID = ownerMailID;
        OwnerUserID = ownerUserID;
        OptionNum = optionNum;
    }

    public ResultHelper(String question, String option1, String option2, String option3, String questionId, String creationDate, String creationTime, String ownerMailID, String ownerUserID, int optionNum) {
        Question = question;
        Option1 = option1;
        Option2 = option2;
        Option3 = option3;
        QuestionId = questionId;
        CreationDate = creationDate;
        CreationTime = creationTime;
        OwnerMailID = ownerMailID;
        OwnerUserID = ownerUserID;
        OptionNum = optionNum;
    }

    public ResultHelper(String question, String option1, String option2, String option3, String option4, String questionId, String creationDate, String creationTime, String ownerMailID, String ownerUserID, int optionNum) {
        Question = question;
        Option1 = option1;
        Option2 = option2;
        Option3 = option3;
        Option4 = option4;
        QuestionId = questionId;
        CreationDate = creationDate;
        CreationTime = creationTime;
        OwnerMailID = ownerMailID;
        OwnerUserID = ownerUserID;
        OptionNum = optionNum;
    }

    public ResultHelper() {
    }
}
