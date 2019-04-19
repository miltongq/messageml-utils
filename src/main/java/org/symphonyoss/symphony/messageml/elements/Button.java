package org.symphonyoss.symphony.messageml.elements;

import org.apache.commons.lang3.StringUtils;
import org.symphonyoss.symphony.messageml.exceptions.InvalidInputException;
import org.symphonyoss.symphony.messageml.markdown.nodes.ButtonNode;

import java.util.*;


/**
 * Class representing a Symphony Elements button
 *
 * @author lumoura
 * @since 03/21/19
 */
public class Button extends Element {

  public static final String MESSAGEML_TAG = "button";
  public static final String ID_ATTR = "id";
  public static final String TYPE_ATTR = "type";
  public static final Set<String> VALID_CLASSES = new HashSet<>(Arrays.asList("primary", "secondary",
          "primary-destructive", "secondary-destructive"));
  public static final Set<String> VALID_TYPES = new HashSet<>(Arrays.asList("action", "reset"));

  public Button(Element parent) {
    super(parent, MESSAGEML_TAG);
    setAttribute(TYPE_ATTR, "action");
  }

  @Override
  public void buildAttribute(org.w3c.dom.Node item) throws InvalidInputException {
    switch (item.getNodeName()) {
      case ID_ATTR:
        setAttribute(ID_ATTR, getStringAttribute(item).toLowerCase());
        break;

      case TYPE_ATTR:
        setAttribute(TYPE_ATTR, getStringAttribute(item).toLowerCase());
        break;

      case CLASS_ATTR:
        setAttribute(CLASS_ATTR, getStringAttribute(item).toLowerCase());
        break;

      default:
        throw new InvalidInputException("Attribute \"" + item.getNodeName()
                + "\" is not allowed in \"" + getMessageMLTag() + "\"");
    }
  }

  @Override
  public org.commonmark.node.Node asMarkdown() {
    return new ButtonNode();
  }

  @Override
  public void validate() throws InvalidInputException {
    String type = getAttribute(TYPE_ATTR);
    String id = getAttribute(ID_ATTR);
    String clazz = getAttribute(CLASS_ATTR);

    if (this.getParent().getClass() != Form.class) {
      throw new InvalidInputException("A \"button\" element can only be a child of a \"form\" element");
    }
    if (!VALID_TYPES.contains(type)) {
      throw new InvalidInputException("Attribute \"type\" must be \"action\" or \"reset\"");
    }
    if (clazz != null && !VALID_CLASSES.contains(clazz)) {
      throw new InvalidInputException("Attribute \"class\" must be \"primary\", \"secondary\", " +
              "\"primary-destructive\" or \"secondary-destructive\"");
    }
    if (type.equals("action") && StringUtils.isBlank(id)) {
      throw new InvalidInputException("Attribute \"id\" is required for generic action buttons");
    }
    assertContentModel(Collections.<Class<? extends Element>>singleton(TextNode.class));
  }
}