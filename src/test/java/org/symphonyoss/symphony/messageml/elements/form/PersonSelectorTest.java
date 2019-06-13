package org.symphonyoss.symphony.messageml.elements.form;

import org.junit.Test;
import org.symphonyoss.symphony.messageml.elements.Element;
import org.symphonyoss.symphony.messageml.elements.ElementTest;
import org.symphonyoss.symphony.messageml.elements.Form;
import org.symphonyoss.symphony.messageml.elements.MessageML;
import org.symphonyoss.symphony.messageml.elements.PersonSelector;
import org.symphonyoss.symphony.messageml.exceptions.InvalidInputException;

import static org.junit.Assert.assertEquals;

public class PersonSelectorTest extends ElementTest {
  private static final String FORM_ID_ATTR = "id";
  
  @Test
  public void sendValidPersonSelectorOnPresentationML() throws Exception {
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><div class=\"person-selector\"/></form></messageML>", null, MessageML.MESSAGEML_VERSION);
    assertDataFromValidParsedTag();
  }

  @Test
  public void sendInvalidAttrPersonSelectorOnPresentationML() throws Exception {
    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Attribute \"id\" is not allowed in \"person-selector\"");
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><div id=\"idOne\" class=\"person-selector\"/></form></messageML>", null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void sendInvalidContentPersonSelectorOnPresentationML() throws Exception {
    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Element \"person-selector\" may not have child elements or text content");
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><div class=\"person-selector\"><div>hey</div></div></form></messageML>", null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void sendValidPersonSelector() throws Exception {
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><person-selector/></form></messageML>", null, MessageML.MESSAGEML_VERSION);
    assertDataFromValidParsedTag();
  }

  @Test
  public void sendValidPersonSelectorWithClosingTag() throws Exception {
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><person-selector></person-selector></form></messageML>", null, MessageML.MESSAGEML_VERSION);
    assertDataFromValidParsedTag();
  }

  @Test
  public void sendPersonSelectorWithChildElement() throws Exception {
    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Element \"person-selector\" may not have child elements or text content");
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><person-selector>a</person-selector></form></messageML>", null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void sendPersonSelectorWithAttribute() throws Exception {
    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Element \"person-selector\" may not have attributes");
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><person-selector class=\"some-class\"/></form></messageML>", null, MessageML.MESSAGEML_VERSION);
  }

  @Test
  public void sendPersonSelectorOutsideForm() throws Exception {
    expectedException.expect(InvalidInputException.class);
    expectedException.expectMessage("Element \"person-selector\" can only be a child of the following elements: [form]");
    context.parseMessageML("<messageML><form id=\"" + FORM_ID_ATTR + "\"><div><person-selector/></div></form></messageML>", null, MessageML.MESSAGEML_VERSION);
  }

  private void assertDataFromValidParsedTag() {
    MessageML messageML = context.getMessageML();
    Element form = messageML.getChildren().get(0);
    Element personSelector = form.getChildren().get(0);
    assertEquals(form.getClass(), Form.class);
    assertEquals(personSelector.getClass(), PersonSelector.class);
    assertEquals("<div data-format=\"PresentationML\" data-version=\"2.0\"><form id=\"" + FORM_ID_ATTR + "\"><div class=\"person-selector\"/></form></div>", context.getPresentationML());
    assertEquals("Form (log into desktop client to answer):\n---\n(Person Selector)\n\n---\n", context.getMarkdown());
  }
}