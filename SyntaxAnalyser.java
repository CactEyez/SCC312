// author Stephen Middlemass
import java.io.IOException;

public class SyntaxAnalyser extends AbstractSyntaxAnalyser
{
    public SyntaxAnalyser(String fileName) throws IOException
    {
        lex = new LexicalAnalyser(fileName);
    }
   
    @Override
    public void _statementPart_() throws IOException, CompilationException
    {
        //This is the start
        myGenerate.commenceNonterminal("StatementPart");
        //This is the start of the list of statements
        acceptTerminal(Token.beginSymbol);

        //This is the end
        acceptTerminal(Token.endSymbol);
        myGenerate.finishNonterminal("StatementPart");
    }
    
    @Override
    public void acceptTerminal(int symbol) throws IOException, CompilationException
    {
        Token t = nextToken;
        //Compare the symbol against the expected symbol
        if(symbol == t.symbol)
        {
            myGenerate.insertTerminal(nextToken);
            //Collect the next token
            nextToken = lex.getNextToken();
            return;
        }
        //If there is an issue where the symbol doesnt match the expected symbol, an error is reported with the expected and given symbols
        myGenerate.reportError(nextToken, "expected: " + Token.getName(symbol) + " but has: " + Token.getName(nextToken.symbol));
    }

    private void statementList() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("StatementList");

        myGenerate.finishNonterminal("StatementList");
    }

    private void statement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Statement");

        myGenerate.finishNonterminal("Statement");
    }

    private void assignmentStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("AssignmentStatement");

        myGenerate.finishNonterminal("AssignmentStatement");
    }

    private void ifStatement() throws CompilationException, IOException
    {
        //if <condition> then <statement list> end if | if <condition> then <statement list> else <statement list> end if

        myGenerate.commenceNonterminal("IfStatement");
        
        //if
        acceptTerminal(Token.ifSymbol);

        //<condition>
        condition();

        //then
        acceptTerminal(Token.thenSymbol);

        //<statement list>
        statementList();

        //else
        acceptTerminal(Token.elseSymbol);

        //<statement list>
        statementList();

        //end
        acceptTerminal(Token.endSymbol);

        acceptTerminal(Token.ifSymbol);

        myGenerate.finishNonterminal("IfStatement");
    }

    private void whileStatement() throws CompilationException, IOException
    {
        //while <condition> loop <statement list> end loop
        
        myGenerate.commenceNonterminal("WhileStatement");

        //while
        acceptTerminal(Token.whileSymbol);

        //<condition>
        condition();

        //loop
        acceptTerminal(Token.loopSymbol);

        //<statement list>
        statementList();

        //end
        acceptTerminal(Token.endSymbol);

        //loop
        acceptTerminal(Token.loopSymbol);

        myGenerate.finishNonterminal("WhileStatement");
    }

    private void procedureStatement() throws CompilationException, IOException
    {
        //call identifier ( <argument list> )

        myGenerate.commenceNonterminal("ProcedureStatement");

        //call
        acceptTerminal(Token.callSymbol);

        //identifier
        acceptTerminal(Token.identifier);

        //(
        acceptTerminal(Token.leftParenthesis);

        //<argument list>
        argumentList();

        //)
        acceptTerminal(Token.rightParenthesis);

        myGenerate.finishNonterminal("ProcedureStatement");
    }

    private void untilStatement() throws CompilationException, IOException
    {
        //do <statement list> until <condition>

        myGenerate.commenceNonterminal("UntilStatement");

        //do
        acceptTerminal(Token.doSymbol);

        //<statement list>
        statementList();

        //until
        acceptTerminal(Token.untilSymbol);

        //<condition>
        condition();

        myGenerate.finishNonterminal("UntilStatement");
    }

    private void forStatement() throws CompilationException, IOException
    {
        //for ( <assignment statement> ; <condition> ; <assignment statement> ) do <statement list> end loop

        myGenerate.commenceNonterminal("ForStatement");

        //for
        acceptTerminal(Token.forSymbol);

        //(
        acceptTerminal(Token.leftParenthesis);

        //<assignment statement>
        assignmentStatement();

        //;
        acceptTerminal(Token.semicolonSymbol);

        //<condition>
        condition();

        //;
        acceptTerminal(Token.semicolonSymbol);

        //<assignment statement>
        assignmentStatement();
        
        //)
        acceptTerminal(Token.rightParenthesis);

        //do
        acceptTerminal(Token.doSymbol);

        //<statement list>
        statementList();

        //end
        acceptTerminal(Token.endSymbol);

        //loop
        acceptTerminal(Token.loopSymbol);

        myGenerate.finishNonterminal("ForStatement");
    }

    private void argumentList() throws CompilationException, IOException
    {
        //identifier | <argument list> , identifier
        
        myGenerate.commenceNonterminal("ArgumentList");

        //identifier
        acceptTerminal(Token.identifier);

        // | ,
        while (nextToken.symbol == Token.commaSymbol)
        {
            //,
            acceptTerminal(Token.commaSymbol);

            //indentifier
            acceptTerminal(Token.identifier);
        }

        myGenerate.finishNonterminal("ArgumentList");
    }

    private void condition() throws CompilationException, IOException
    {
        //identifier <conditional operator> identifier | identifier <conditional operator> numberConstant | identifier <conditional operator> stringConstant
        //identifier <conditional operator> identifier|numberConstant|stringConstant
        myGenerate.commenceNonterminal("Condition");

        //identifier
        acceptTerminal(Token.identifier);

        //<conditional operator>
        conditionalOperator();

        //identifier|numberConstant|stringConstant
        switch(nextToken.symbol)
        {
            //identifier
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            //numberConstant
            case Token.numberConstant:
                acceptTerminal(Token.identifier);
                break;
            //stringConstant
            case Token.stringConstant:
                acceptTerminal(Token.stringConstant);
                break;
            default:
        }

        myGenerate.finishNonterminal("Condition");
    }

    private void conditionalOperator() throws CompilationException, IOException
    {
        //>|>=|=|/=|<|<=
        
        myGenerate.commenceNonterminal("ConditionalOperator");

        switch(nextToken.symbol)
        {
            //>
            case Token.lessThanSymbol:
                acceptTerminal(Token.lessThanSymbol);
                break;
            //>=
            case Token.lessEqualSymbol:
                acceptTerminal(Token.lessEqualSymbol);
                break;
            //=
            case Token.equalSymbol:
                acceptTerminal(Token.equalSymbol);
                break;
            ///+
            case Token.notEqualSymbol:
                acceptTerminal(Token.notEqualSymbol);
                break;
            //<
            case Token.greaterThanSymbol:
                acceptTerminal(Token.greaterThanSymbol);
                break;
            //<=
            case Token.greaterEqualSymbol:
                acceptTerminal(Token.greaterEqualSymbol);
                break;
            default:
        }

        myGenerate.finishNonterminal("ConditionalOperator");
    }

    private void expression() throws CompilationException, IOException
    {
        //<term> | <expression> + <term> | <expression> - <term>
        //         <expression> +|- <term>
        
        myGenerate.commenceNonterminal("Expression");

        //<term>
        term();

        //+|-
        while(nextToken.symbol == Token.plusSymbol | nextToken.symbol == Token.minusSymbol)
        {
            //+|-
            acceptTerminal(nextToken.symbol);

            //<term>
            term();
        }

        myGenerate.finishNonterminal("Expression");
    }

    private void term() throws CompilationException, IOException
    {
        //<factor> | <term> * <factor> | <term> / <factor>
        //           <term> *|/ <factor>
        
        myGenerate.commenceNonterminal("Term");

        //<factor>
        factor();

        //*|/
        while(nextToken.symbol == Token.timesSymbol | nextToken.symbol == Token.divideSymbol)
        {
            //*|/
            acceptTerminal(nextToken.symbol);

            //<factor
            factor();
        }

        myGenerate.finishNonterminal("Term");
    }

    private void factor() throws CompilationException, IOException
    {
        //identifier | numberConstant | ( <expression> )

        myGenerate.commenceNonterminal("Factor");

        //|
        switch(nextToken.symbol)
        {
            //indentifier
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            //numberConstant
            case Token.numberConstant:
                acceptTerminal(Token.numberConstant);
                break;
            //( <expression> )
            case Token.leftParenthesis:
                acceptTerminal(Token.leftParenthesis);
                expression();
                acceptTerminal(Token.rightParenthesis);
            default:
        }

        myGenerate.finishNonterminal("Factor");
    }
}
