/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transtool.quiz;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import transtool.questions.BrainhoneyContents;

/**
 *
 * @author hallm8
 */
public class MultipleChoice extends BrainhoneyQuestion {

    /**
     * 
     * @param brain
     * @param document
     * @param id
     * @param item
     * @param root 
     */
    public MultipleChoice(BrainhoneyContents brain, Document document, int id, int item, Element root) {
        super(brain, document, brain.getQuestionID(), item, root);
        writeHeader();

        //Field entries are very question specific.  Well... for the question type.
        Element fieldEntry2 = doc.createElement("fieldentry");
        if (brainhoney.getqChoice().size() == 2  && (brainhoney.getqChoice().get(0).equalsIgnoreCase("True") || brainhoney.getqChoice().get(0).equalsIgnoreCase("False"))
                && (brainhoney.getqChoice().get(1).equalsIgnoreCase("True") || brainhoney.getqChoice().get(1).equalsIgnoreCase("False"))){
            fieldEntry2.appendChild(doc.createTextNode("True/False"));
        }
        else{
        fieldEntry2.appendChild(doc.createTextNode("Multiple Choice"));
        }
        Element qtiDataField2 = (Element) rootItem.getElementsByTagName("qti_metadatafield").item(1);
        qtiDataField2.appendChild(fieldEntry2);

        Element presentation = (Element) rootItem.getElementsByTagName("presentation").item(0);
        Element flow = doc.createElement("flow");
        presentation.appendChild(flow);

        Element resprocessing = doc.createElement("resprocessing");
        rootItem.appendChild(resprocessing);
        // Carrying on from the other thingamajig.  I don't know what I am trying to say.
        Element material = doc.createElement("material");
        Element extension = doc.createElement("response_extension");
        Element lid = doc.createElement("response_lid");

        flow.appendChild(material);
        flow.appendChild(extension);
        flow.appendChild(lid);

        Element matText = doc.createElement("mattext");
        material.appendChild(matText);

        matText.setAttribute("texttype", "text/html");
        matText.appendChild(doc.createTextNode(brainhoney.getBody()));

        Element dStyle = doc.createElement("d2l_2p0:display_style");
        Element enumeration = doc.createElement("d2l_2p0:enumeration");
        Element gType = doc.createElement("d2l_2p0:grading_type");
        extension.appendChild(dStyle);
        extension.appendChild(enumeration);
        extension.appendChild(gType);

        dStyle.appendChild(doc.createTextNode("2"));
        enumeration.appendChild(doc.createTextNode("5"));
        gType.appendChild(doc.createTextNode("0"));

        Attr identity = doc.createAttribute("ident");
        Attr rcardinality = doc.createAttribute("rcardinality");
        identity.setValue(brain.getQuestionID() + "_LID");
        rcardinality.setValue("Single");
        lid.setAttributeNode(identity);
        lid.setAttributeNode(rcardinality);

        Element renderChoice = doc.createElement("render_choice");
        renderChoice.setAttribute("shuffle", "no");
        lid.appendChild(renderChoice);

        for (String qChoice : brainhoney.getqChoice()) {

            Element flowLabel = doc.createElement("flow_label");
            renderChoice.appendChild(flowLabel);

            Attr classLabel = doc.createAttribute("class");
            classLabel.setValue("Block");
            flowLabel.setAttributeNode(classLabel);

            Element responseLabel = doc.createElement("response_label");
            responseLabel.setAttribute("ident", brain.getQuestionID() + "_A" + itemNumber);
            Element flowMat = doc.createElement("flow_mat");
            Element mbody = doc.createElement("material");
            Element materialText = doc.createElement("mattext");
            materialText.setAttribute("texttype", "text/html");

            materialText.appendChild(doc.createTextNode(qChoice));

            flowLabel.appendChild(responseLabel);
            responseLabel.appendChild(flowMat);
            flowMat.appendChild(mbody);
            mbody.appendChild(materialText);
            itemNumber++;
        }

        for (int j = 0; j < brainhoney.getqChoice().size(); j++) {
            Element respCondition = doc.createElement("respcondition");
            Element conditionvar = doc.createElement("conditionvar");
            Element varequal = doc.createElement("varequal");
            Element decVar = doc.createElement("decvar");
            Element setVariable = doc.createElement("setvar");

            respCondition.setAttribute("title", "Response Condition " + Integer.toString(j + 1));
            varequal.setAttribute("respident", brain.getQuestionID() + "_LID");
            varequal.appendChild(doc.createTextNode(brain.getQuestionID() + "_A" + questionNumber));
            setVariable.setAttribute("action", "Set");


            
            if (!brainhoney.getRightAnswer().isEmpty()) {
                if (brainhoney.getRightAnswer().get(0).equals(Integer.toString(j + 1))) {
                    setVariable.appendChild(doc.createTextNode("100.000000000"));
                } else {
                    setVariable.appendChild(doc.createTextNode("0.000000000"));
                }
            }

            Element displayFeedback = doc.createElement("displayfeedback");
            displayFeedback.setAttribute("feedbacktype", "Response");
            displayFeedback.setAttribute("linkrefid", brain.getQuestionID() + "_IF" + questionNumber);
            questionNumber++;

            respCondition.appendChild(displayFeedback);
            resprocessing.appendChild(respCondition);
            respCondition.appendChild(conditionvar);
            conditionvar.appendChild(varequal);
            respCondition.appendChild(setVariable);

        }

        // Right here holds item feedback for the questions.  They
        // are associated with each question via feedback ID.
        for (String qChoice : brainhoney.getqChoice()) {
            Element itemFeedback = doc.createElement("itemfeedback");
            itemFeedback.setAttribute("ident", brain.getQuestionID() + "_IF" + feedbackNumber);
            Element fMaterial = doc.createElement("material");
            Element mattext = doc.createElement("mattext");
            mattext.setAttribute("texttype", "text/html");

            rootItem.appendChild(itemFeedback);
            itemFeedback.appendChild(fMaterial);
            fMaterial.appendChild(mattext);
            feedbackNumber++;
        }
    }
}
