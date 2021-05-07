package css.project.XMLparser;

import css.project.Utils.Tuple;
import css.project.bigNumber.BigNumber;
import css.project.exception.custom.ParsingException;

import java.util.Hashtable;

public class XMLParser {

    // <expression> </expression> equivalent of "(" ")"
    // <operation> +,-,/,*,^ </operation>
    // <variable> a,b,... or "(0.5)" --> in this case it will be preceded by "^" operation </variable>

    public static Tuple<String,Hashtable<String,BigNumber>> GetExpressionFromXML(String rawXMLContent)
    {
        String xmlContent = rawXMLContent.replaceAll("\\s+","")
                  .replaceAll("\\n+","")
                  .replaceAll("\\r+","");
        Hashtable<String, BigNumber> values = new Hashtable<>();
        String currentVariableValue = "";

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
                                break;
                            }
                            else {
                                expression.append(xmlContent.charAt(currentCharacterPosition));
                                currentCharacterPosition += 12; //jumping over
                                break;
                            }
                        case "variableValue":
                            currentVariableValue= String.valueOf(xmlContent.charAt(currentCharacterPosition));
                            currentCharacterPosition+=17;
                            break;
                        case "value":
                            StringBuilder bigNumber = new StringBuilder();
                            while ( xmlContent.charAt(currentCharacterPosition)!= '<') {
                                bigNumber.append(xmlContent.charAt(currentCharacterPosition));
                                currentCharacterPosition++;
                            }
                            values.put(currentVariableValue,new BigNumber(bigNumber.toString()));
                            currentCharacterPosition += 8;
                        default:
                            break;
                    }
                }
            else
                throw new ParsingException("A tag is not properly closed or written");
        }
        return new Tuple<String,Hashtable<String, BigNumber>>(expression.toString(), values);
    }
}
