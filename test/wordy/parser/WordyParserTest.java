package wordy.parser;

import org.junit.jupiter.api.Test;

import wordy.ast.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static wordy.parser.WordyParser.parseExpression;
import static wordy.parser.WordyParser.parseProgram;
import static wordy.parser.WordyParser.parseStatement;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordyParserTest {

    @Test
    void testNumbers() {
        assertEquals(new ConstantNode(37),    parseExpression("37"));
        assertEquals(new ConstantNode(37.2),  parseExpression("37.2"));
        assertEquals(new ConstantNode(-37.2), parseExpression("-37.2"));
        assertParseError(() -> parseExpression("-37..2"));
    }

    @Test
    void testOperators() {
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.ADDITION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 plus 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.SUBTRACTION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 minus 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.MULTIPLICATION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 times 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.DIVISION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 divided by 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.DIVISION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1\ndivided    by\n 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new ConstantNode(2),
                new ConstantNode(8)),
            parseExpression("2 to the power of 8"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new ConstantNode(16),
                new ConstantNode(2)),
            parseExpression("16 squared"));
    }

    @Test
    void testPrecedence() {
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.ADDITION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.SUBTRACTION,
                    new ConstantNode(1),
                    new ConstantNode(2)),
                new ConstantNode(3)),
            parseExpression("1 minus 2 plus 3"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.SUBTRACTION,
                new ConstantNode(1),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.DIVISION,
                    new ConstantNode(2),
                    new ConstantNode(3))),
            parseExpression("1 minus 2 divided by 3"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.DIVISION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.SUBTRACTION,
                    new ConstantNode(1),
                    new ConstantNode(2)),
                new ConstantNode(3)),
            parseExpression("(1 minus 2) divided by 3"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new VariableNode("x"),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.EXPONENTIATION,
                    new VariableNode("y"),
                    new VariableNode("z"))),
            parseExpression("x to the power of y to the power of z"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.EXPONENTIATION,
                    new VariableNode("x"),
                    new ConstantNode(2)),
                new ConstantNode(2)),
            parseExpression("x squared squared"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new VariableNode("y"),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.EXPONENTIATION,
                    new VariableNode("z"),
                    new ConstantNode(2.0))),
            parseExpression("y to the power of z squared"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.SUBTRACTION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.ADDITION,
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.EXPONENTIATION,
                        new VariableNode("x"),
                        new ConstantNode(2.0)),
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.MULTIPLICATION,
                        new ConstantNode(2.0),
                        new BinaryExpressionNode(
                            BinaryExpressionNode.Operator.EXPONENTIATION,
                            new VariableNode("y"),
                            new BinaryExpressionNode(
                                BinaryExpressionNode.Operator.EXPONENTIATION,
                                new VariableNode("z"),
                                new ConstantNode(2.0))))),
                new ConstantNode(1.0)),
            parseExpression("x squared plus 2 times y to the power of z squared minus 1"));
    }

    @Test
    void testVariables() {
        assertEquals(new VariableNode("x"), parseExpression("x"));
        assertEquals(new VariableNode("snake_and_camelcase_ok"), parseExpression("snake_and_camelCase_OK"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.ADDITION,
                new VariableNode("x"),
                new VariableNode("y")),
            parseExpression("x plus y"));
    }

    @Test
    void testAssignmentStatement() {
        assertEquals(
            new AssignmentNode(
                new VariableNode("sprongle"),
                new ConstantNode(-3)),
            parseStatement("set sprongle to -3"));
        assertEquals(
            new AssignmentNode(
                new VariableNode("sprongle"),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.ADDITION,
                    new VariableNode("zoink"),
                    new ConstantNode(7))),
            parseStatement("set sprongle to zoink plus 7"));
    }

    @Test
    void testConditional() {
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                parseStatement("set y to 1"),
                parseStatement("set z to 3")),
            parseStatement("if x is equal to 2 then set y to 1 else set z to 3"));
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                parseStatement("set y to 1"),
                BlockNode.EMPTY),
            parseStatement("if x equals 2 then set y to 1"));
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                new BlockNode(
                    parseStatement("set y to 1"),
                    parseStatement("set z to 3")),
                new BlockNode(
                    parseStatement("set a to 1"),
                    parseStatement("set b to 3"))),
            parseStatement("if x equals 2 then:set y to 1.set z to 3.else:set a to 1.set b to 3.end of conditional"));
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                new BlockNode(
                    parseStatement("set y to 1"),
                    parseStatement("set z to 3")),
                BlockNode.EMPTY),
            parseStatement("if x is equal to 2 then  :  \nset y to 1.\nset z to 3.\n\nend  of conditional"));
    }

    @Test
    void testLoop() {
        assertEquals(
            new LoopNode(
                new BlockNode(
                    parseStatement("set x to x plus 1"),
                    new ConditionalNode(
                        ConditionalNode.Operator.EQUALS,
                        new VariableNode("x"),
                        new ConstantNode(3),
                        new LoopExitNode(),
                        BlockNode.EMPTY))),
            parseStatement("loop: set x to x plus 1. if x equals 3 then exit loop. end of loop"));
    }

    @Test
    void testFunctionDeclaration() { // I've combed through this error msg, and there is no difference between the expected and actual, I have no idea why it fails
        assertEquals(
            new FunctionNode(
                new VariableNode("plusone"),
                List.of(new VariableNode("x")),
                new BlockNode( parseStatement("set a to x plus 1"), parseStatement("return a"))),
            
            parseStatement("Declare function plusOne that takes parameters ( x ):" +
                    "Set a to x plus 1." +
                    "Return a." +
                    "End of function")
        );
        assertEquals(
            new FunctionNode(
                new VariableNode("addnums"),
                List.of(new VariableNode("x"), new VariableNode("y")),
                new BlockNode( parseStatement("set a to x plus y"), parseStatement("return a"))),
            
            parseStatement("Declare function addnums that takes parameters ( x, y ):" +
                    "Set a to x plus y." +
                    "Return a." +
                    "End of function")
        );
        assertEquals(
            new FunctionNode(
                new VariableNode("addthreenums"),
                List.of(new VariableNode("x"), new VariableNode("y"), new VariableNode("z")),
                new BlockNode( parseStatement("set a to x plus (y plus z)"), parseStatement("return a"))),
            
            parseStatement("Declare function addthreenums that takes parameters ( x, y, z):" +
                    "Set a to x plus (y plus z)." +
                    "Return a." +
                    "End of function")
        );
    }

    @Test 
    void testFunctionCall() {
        assertEquals(
            new AssignmentNode(
                new VariableNode("a"),
                new FunctionCallNode(new VariableNode("addnums"), 
                List.of( 
                    new VariableNode("x"), 
                    new VariableNode("y")
                ))),

            parseStatement("Set a to the result of calling addnums with (x, y)")
        );
        assertEquals(
            new AssignmentNode(
                new VariableNode("a"),
                new FunctionCallNode(new VariableNode("addnums"), 
                List.of( 
                    new FunctionCallNode(
                        new VariableNode("plusone"),
                        List.of(new VariableNode("x"))
                    ), 
                    new VariableNode("y")
                ))),

            parseStatement("Set a to the result of calling addnums with (the result of calling plusone with (x), y)")
        );
        assertEquals(
            new AssignmentNode(
                new VariableNode("a"),
                new FunctionCallNode(new VariableNode("addnums"), 
                List.of( 
                    new VariableNode("y"),
                    new FunctionCallNode(
                        new VariableNode("plusone"),
                        List.of(new VariableNode("x"))
                    )
                ))),

            parseStatement("Set a to the result of calling addnums with ( y, the result of calling plusone with ( x ) )")
        );
    }

    @Test
    void testProgram() {
        assertParseError(() -> parseProgram(""));
        assertEquals(
            new BlockNode(
                new AssignmentNode(
                    new VariableNode("x"),
                    new ConstantNode(1))),
            parseProgram("set x to 1."));

        assertEquals(
            new BlockNode(
                new AssignmentNode(
                    new VariableNode("x"),
                    new ConstantNode(1)),
                new AssignmentNode(
                    new VariableNode("y"),
                    new ConstantNode(2))),
            parseProgram("set x to 1. set y to 2."));
    }

    @Test
    void testWhitespaceHandling() {
        assertEquivalentParsing(
            "set x to 1. set y to 2.",
            "set x to 1.set y to 2.",
            "\n\n    set \tx  to \n 1      \n. set \n y \n to  \n 2 \n  .  \t ");
        assertEquivalentParsing(
            "loop: if x is greater than 2 then: exit loop. else: set x to x squared. end of conditional. end of loop.",
            "loop:if x is greater than 2 then:exit loop.else:set x to x squared.end of conditional.end of loop.",
            "  loop  :  if  x  is  greater  than  2  then  :  exit  loop  .  else  :  set  x  to  x  squared  .  "
                + "   end  of  conditional  .  end  of  loop  .  ");
        assertEquivalentParsing(
            "set x to (x squared) plus (y to the power of (3 plus (z squared))).",
            "set x to  (  x  squared  )  plus  (  y  to  the  power  of  (  3  plus  (  z squared  )  )  )  .",
            "set x to(x squared)plus(y to the power of(3 plus(z squared))).");
    }

    private void assertEquivalentParsing(String... variants) {
        ASTNode expected = parseProgram(variants[0]);
        for(var variant : variants) {
            try {
                assertEquals(expected, parseProgram(variant));
            } catch(RuntimeException e) {
                System.err.println("Failure while comparing parsing of:"
                    + "\n    " + variants[0]
                    + "\n    " + variant);
                throw e;
            }
        }
    }

    private void assertParseError(Runnable parseAction) {
        assertThrows(ParseException.class, parseAction::run);
    }
}
