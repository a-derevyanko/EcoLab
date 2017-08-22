package org.ekolab.server.dao.impl.content;

import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.db.h2.public_.tables.records.LabTestQuestionRecord;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabTestQuestion;
import org.ekolab.server.model.content.LabTestQuestionVariant;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.ekolab.server.db.h2.public_.Tables.*;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabDaoImpl<T extends LabData> implements LabDao<T> {
    private static final RecordMapper<Record, LabTestQuestionVariant> LAB_TEST_DATA_MAPPER = record -> {
        LabTestQuestionVariant questionVariant = new LabTestQuestionVariant();
        questionVariant.setQuestion(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_TEXT));
        questionVariant.setAnswers(Arrays.asList((String[])record.get(LAB_TEST_QUESTION_VARIANT.ANSWERS)));
        questionVariant.setRightAnswer(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_ANSWER_NUMBER));
        try (InputStream is = new ByteArrayInputStream(record.get(LAB_TEST_QUESTION_VARIANT.IMAGE))) {
            questionVariant.setImage(ImageIO.read(is));
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        return questionVariant;
    };

    @Autowired
    protected DSLContext dsl;

    protected Select<Record1<Long>> getFindUserIdSelect(String userName) {
        return dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName));
    }

    @Override
    public Collection<LabTestQuestion> getTestQuestions(Locale locale) {
        Map<LabTestQuestionRecord, LabTestQuestionVariant> variants =  dsl.selectFrom(LAB_TEST_QUESTION.join(LAB_TEST_QUESTION_VARIANT).
                on(LAB_TEST_QUESTION.ID.eq(LAB_TEST_QUESTION_VARIANT.QUESTION_ID))).
                where(LAB_TEST_QUESTION.LAB_NUMBER.eq(getLabNumber())).fetchMap(LAB_TEST_QUESTION, LAB_TEST_DATA_MAPPER);

        Map<Integer, LabTestQuestion> questions = new HashMap<>();
        for (Map.Entry<LabTestQuestionRecord, LabTestQuestionVariant> questionVariant : variants.entrySet()) {
            LabTestQuestion question = questions.putIfAbsent(questionVariant.getKey().getQuestionNumber(), new LabTestQuestion());
            question.setTitle(questionVariant.getKey().getQuestionTitle());

            if (question.getVariants() == null) {
                question.setVariants(new ArrayList<>());
            }
            question.getVariants().add(questionVariant.getValue());
        }
        return questions.values();
    }

    protected abstract int getLabNumber();
}
