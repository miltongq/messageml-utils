package org.symphonyoss.symphony.messageml.elements.form;

import org.junit.Before;
import org.junit.Test;
import org.symphonyoss.symphony.messageml.MessageMLContext;
import org.symphonyoss.symphony.messageml.elements.Checkbox;
import org.symphonyoss.symphony.messageml.elements.Element;
import org.symphonyoss.symphony.messageml.elements.ElementTest;
import org.symphonyoss.symphony.messageml.elements.Form;
import org.symphonyoss.symphony.messageml.elements.MessageML;
import org.symphonyoss.symphony.messageml.exceptions.InvalidInputException;

import static org.junit.Assert.assertEquals;

public class CheckboxTest extends ElementTest {
  private String formId;
  private String name;
  private String value;
  private String text;
  private String checked;

  @Before
  public void beforeEach() {
    this.formId = "checkbox-form";
    this.name = "checkbox-name";
    this.value = "checkbox-value";
    this.text = "Checkbox Text";
    this.checked = "false";
  }

  @Test
  public void testPresentationMLCheckbox() throws Exception {
    String input = String.format("<div data-format=\"PresentationML\" data-version=\"2.0\">" +
        "<form id=\"" + formId + "\">" +
        "<div class=\"checkbox-group\">" +
        "<input type=\"checkbox\" name=\"%s\" value=\"%s\"/>" +
        "<label>%s</label>" +
        "</div></form></div>", this.name, this.value, this.text);

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
    verifyMessageMLObjectsForCheckbox(context);
    verifyCheckboxPresentationML(context, name, value, text, null, false);
    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testPresentationMLCheckboxWithOnlyNameAttribute() throws Exception {
    String input = "<div data-format=\"PresentationML\" data-version=\"2.0\">" +
        "<form id=\"checkbox-form\">" +
        "<input type=\"checkbox\" name=\"checkbox-name\"/>" +
        "</form></div>";

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);

    verifyMessageMLObjectsForCheckbox(context);
    String presentationML = context.getPresentationML();
    String expectedPresentationML ="<div data-format=\"PresentationML\" data-version=\"2.0\"><form id=\"checkbox-form\"><input type=\"checkbox\" name=\"checkbox-name\" value=\"on\"/></form></div>";
    assertEquals(expectedPresentationML, presentationML);

    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testInvalidPresentationMLCheckbox() throws Exception {
    String input = String.format("<div data-format=\"PresentationML\" data-version=\"2.0\">" +
        "<form id=\"" + formId + "\">" +
        "<div class=\"checkbox-group\">" +
        "<input type=\"checkbox\" name=\"%s\" value=\"%s\"/>" +
        "<label>%s</label><label>other</label>" +
        "</div></form></div>", this.name, this.value, this.text);

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Invalid PresentationML for the \"checkbox\" element");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testInvalidAttrPresentationMLCheckbox() throws Exception {
    String input = "<div data-format=\"PresentationML\" data-version=\"2.0\">" +
        "<form id=\"" + formId + "\">" +
        "<div class=\"checkbox-group\">" +
        "<input id=\"id1\" type=\"checkbox\" name=\"name2\" value=\"value1\"/>" +
        "<label>Text 1</label>" +
        "</div></form></div>";

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Attribute \"id\" is not allowed in \"checkbox\"");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testInvalidPresentationMLCheckboxTwoInputs() throws Exception {
    String input = "<div data-format=\"PresentationML\" data-version=\"2.0\">" +
        "<form id=\"" + formId + "\">" +
        "<div class=\"checkbox-group\">" +
        "<input type=\"checkbox\" name=\"name2\" value=\"value1\"/>" +
        "<input type=\"checkbox\" name=\"name2\" value=\"value2\"/>" +
        "</div></form></div>";

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Invalid PresentationML for the \"checkbox\" element");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testInvalidPresentationMLCheckboxTwoLabels() throws Exception {
    String input = "<div data-format=\"PresentationML\" data-version=\"2.0\">" +
        "<form id=\"" + formId + "\">" +
        "<div class=\"checkbox-group\">" +
        "<label>Text 1</label>" +
        "<label>Text 2</label>" +
        "</div></form></div>";

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Invalid PresentationML for the \"checkbox\" element");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testCompleteFilledCheckbox() throws Exception {
    checked = "true";
    String input = buildMessageMLFromParameters(name, value, text, checked, true);
    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
    verifyMessageMLObjectsForCheckbox(context);
    verifyCheckboxPresentationML(context, name, value, text, checked, true);
    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testNonCheckedCompleteCheckbox() throws Exception {
    String input = buildMessageMLFromParameters(name, value, text, checked, true);
    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
    verifyMessageMLObjectsForCheckbox(context);
    verifyCheckboxPresentationML(context, name, value, text, checked, true);
    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testNoCheckedParameterCheckbox() throws Exception {
    String input = buildMessageMLFromParameters(name, value, text, checked, false);
    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
    verifyMessageMLObjectsForCheckbox(context);
    verifyCheckboxPresentationML(context, name, value, text, checked, false);
    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testNoValueParameterCheckbox() throws Exception {
    String input = buildMessageMLFromParameters(name, null, text, checked, true);
    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
    verifyMessageMLObjectsForCheckbox(context);
    verifyCheckboxPresentationML(context, name, null, text, checked, true);
    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testSimplerCheckbox() throws Exception {
    String input = buildMessageMLFromParameters(name, null, text, checked, false);
    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
    verifyMessageMLObjectsForCheckbox(context);
    verifyCheckboxPresentationML(context, name, null, text, checked, false);
    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testCheckboxWithoutName() throws Exception {
    String input = buildMessageMLFromParameters(null, value, text, checked, true);

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("The attribute \"name\" is required");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testCheckboxWithoutAny() throws Exception {
    String input = buildMessageMLFromParameters(null, null, null, "false", false);

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("The attribute \"name\" is required");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testCheckboxWithNonTextContent() throws Exception {
    String input = "<messageML><form id=\"" + formId + "\"><checkbox name=\"name\" value=\"value\"><div>Value</div></checkbox></form></messageML>";

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Element \"div\" is not allowed in \"checkbox\"");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testNoTextParameterCheckbox() throws Exception {
    String input = buildMessageMLFromParameters(name, value, null, checked, true);

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);

    verifyMessageMLObjectsForCheckbox(context);
    String presentationML = context.getPresentationML();
    String expectedPresentationML ="<div data-format=\"PresentationML\" data-version=\"2.0\"><form id=\"checkbox-form\"><input type=\"checkbox\" name=\"checkbox-name\" checked=\"false\" value=\"checkbox-value\"/></form></div>";
    assertEquals(expectedPresentationML, presentationML);

    verifyCheckboxMarkdown(context, name);
  }

  @Test
  public void testCheckboxWithInvalidValueForChecked() throws Exception {
    String input = buildMessageMLFromParameters(name, value, text, "somethingElse", true);

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Attribute \"checked\" of element \"checkbox\" can only be one of the following values: [true, false].");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testCheckboxWithoutForm() throws Exception {
    String input = "<messageML><checkbox value=\"value\">Value</checkbox></messageML>";

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Element \"checkbox\" can only be a inner child of the following elements: [form]");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void testCheckboxWithInvalidAttribute() throws Exception {
    String input = "<messageML><form id=\"" + formId + "\"><checkbox invalid=\"true\" value=\"value\">Value</checkbox></form></messageML>";

    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Attribute \"invalid\" is not allowed in \"checkbox\"");

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
  }

  private String buildMessageMLFromParameters(String name, String value, String text, String checked, boolean shouldSendCheckedAttribute) {
    return "<messageML><form id=\"" + formId + "\"><checkbox" +
        (name != null ? String.format(" name=\"%s\"", name) : "") +
        (value != null ? String.format(" value=\"%s\"", value) : "") +
        (shouldSendCheckedAttribute ? String.format(" checked=\"%s\"", checked) : "") +
        ">" +
        (text != null ? text : "") +
        "</checkbox></form></messageML>";
  }

  private void verifyMessageMLObjectsForCheckbox(MessageMLContext context) {
    MessageML messageML = context.getMessageML();
    Element form = messageML.getChildren().get(0);
    Element checkbox = form.getChildren().get(0);
    assertEquals(form.getClass(), Form.class);
    assertEquals(checkbox.getClass(), Checkbox.class);
  }

  private void verifyCheckboxPresentationML(MessageMLContext context, String name, String value, String text, String checked, boolean shouldShowChecked) {
    String presentationML = context.getPresentationML();
    String expectedPresentationML = buildExpectedPresentationMLForCheckbox(name, value, text, checked, shouldShowChecked);
    assertEquals(expectedPresentationML, presentationML);
  }

  private String buildExpectedPresentationMLForCheckbox(String name, String value, String text, String checked, boolean shouldShowChecked) {
    return "<div data-format=\"PresentationML\" data-version=\"2.0\"><form id=\"" + formId + "\"><div class=\"checkbox-group\"><input type=\"checkbox\"" +
        String.format(" name=\"%s\"", name) +
        (shouldShowChecked ? String.format(" checked=\"%s\"", checked) : "") +
        (value != null ? String.format(" value=\"%s\"", value) : " value=\"on\"") +
        "/><label>" +
        (text != null ? text : "") +
        "</label></div></form></div>";
  }

  private void verifyCheckboxMarkdown(MessageMLContext context, String name) {
    String markdown = context.getMarkdown();
    String expectedMarkdown  = buildExpectedMarkdownForCheckbox(name);
    assertEquals(expectedMarkdown, markdown);
  }

  private String buildExpectedMarkdownForCheckbox(String name) {
    return String.format("Form (log into desktop client to answer):\n---\n(Checkbox:%s)\n---\n", name);
  }
}
