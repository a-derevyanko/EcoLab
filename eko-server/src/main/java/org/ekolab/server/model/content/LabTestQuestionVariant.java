package org.ekolab.server.model.content;

import java.util.List;
import java.util.Objects;

public class LabTestQuestionVariant extends LabTestQuestion {
    private List<String> answers;
    private int rightAnswer;

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabTestQuestionVariant)) return false;
        LabTestQuestionVariant that = (LabTestQuestionVariant) o;
        return rightAnswer == that.rightAnswer &&
                Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers, rightAnswer);
    }
}
