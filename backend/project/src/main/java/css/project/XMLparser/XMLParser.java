package css.project.XMLparser;

public class XMLParser {

    // <expression> </expression> equivalent of "(" ")"
    // <operation> +,-,/,*,^ </operation>
    // <variable> a,b,... or "(0.5)" --> in this case it will be preceded by "^" operation </variable>

    public static String GetExpressionFromXML(String rawXMLContent)
    {
        String xmlContent = rawXMLContent.replaceAll("\\s+","")
                  .replaceAll("\\n+","")
                  .replaceAll("\\r+","");

        int currentCharacterPosition = 0;
        StringBuilder expression = new StringBuilder();

        while (currentCharacterPosition < xmlContent.length()) {
            if (xmlContent.charAt(currentCharacterPosition) == '<')  {
                    StringBuilder tag = new StringBuilder();
                    currentCharacterPosition++;
                    while ( xmlContent.charAt(currentCharacterPosition)!= '>') {
                        tag.append(xmlContent.charAt(currentCharacterPosition));
                        currentCharacterPosition++;
                    }
                    currentCharacterPosition++;
                    switch(tag.toString()) {
                        case "expression":
                            expression.append("(");
                            break;
                        case "/expression":
                            expression.append(")");
                            break;
                        case "operation":
                            expression.append(xmlContent.charAt(currentCharacterPosition));
                            currentCharacterPosition+=13; // jumping over </expression>
                            break;
                        case "variable":
                            if(xmlContent.charAt(currentCharacterPosition) == '(') { // case "(0.5)"
                                expression.append("(0.5)");
                                currentCharacterPosition += 16; //jumping over (0.5)</variable>
                            }
                            else {
                                expression.append(xmlContent.charAt(currentCharacterPosition));
                                currentCharacterPosition += 12; //jumping over
                                break;
                            }
                        default:
                            break;
                    }
                }
        }
        return expression.toString();
    }
}
