// author Stephen Middlemass
import java.io.IOException;

public class SyntaxAnalyser extends AbstractSyntaxAnalyser
{
    public String filename;
    
    public SyntaxAnalyser(String fName) throws IOException
    {
        filename = fName;
        lex = new LexicalAnalyser(filename);
    }
   
    private String errorMessage(String expectedTokens, Token token)
    {
        return "line " + token.lineNumber + " from " + filename.substring(filename.lastIndexOf('/') + 1) + ": Expected " + expectedTokens + " but found (" + Token.getName(token.symbol) + ").\n";
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
        //Compare the symbol against the expected symbol
        if(symbol == nextToken.symbol)
        {
            myGenerate.insertTerminal(nextToken);
            //Collect the next token
            nextToken = lex.getNextToken();
            return;
        }
        //If there is an issue where the symbol doesnt match the expected symbol, an error is reported with the expected and given symbols
        myGenerate.reportError(nextToken, errorMessage(Token.getName(symbol), nextToken));
    }

    private void statementList() throws CompilationException, IOException
    {
        //<statement> | <statement list> ; <statement>
        
        myGenerate.commenceNonterminal("StatementList");

        //<statement>
        try{statement();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("Statement", nextToken), ex);}

        //<statement list> ;
        while(nextToken.symbol == Token.semicolonSymbol)
        {
            //;
            acceptTerminal(Token.semicolonSymbol);

            //<statement>
            try{statement();}
            catch(CompilationException ex)
            {throw new CompilationException(errorMessage("Statement", nextToken), ex);}
        }

        myGenerate.finishNonterminal("StatementList");
    }

    private void statement() throws CompilationException, IOException
    {
        //<assignment statement> | <if statement> | <while statement> | <procedure statement> | <until statement> | <for statement>

        myGenerate.commenceNonterminal("Statement");

        //|
        try
        {
            switch(nextToken.symbol)
            {
                //<assignment statement>
                case Token.identifier:
                    assignmentStatement();
                    break;
                //<if statement>
                case Token.ifSymbol:
                    ifStatement();
                    break;
                //<while statement>
                case Token.whileSymbol:
                    whileStatement();
                    break;
                //<procedure statement>
                case Token.procedureSymbol:
                    procedureStatement();
                    break;
                //<until statement>
                case Token.untilSymbol:
                    untilStatement();
                    break;
                //<for statement>
                case Token.forSymbol:
                    forStatement();
                    break;
                default:
                    myGenerate.reportError(nextToken, errorMessage("AssignmentStatement, IfStatement, WhileStatement, ProcedureStatement, UntilStatement or ForStatement", nextToken));
                    break;
            }
        }
        catch(CompilationException ex)
        {
            throw new CompilationException(errorMessage("AssignmentStatement, IfStatement, WhileStatement, ProcedureStatement, UntilStatement or ForStatement", nextToken), ex);
        }

        myGenerate.finishNonterminal("Statement");
    }

    private void assignmentStatement() throws CompilationException, IOException
    {
        //identifier := <expression> | identifier := stringConstant
        //              <expression>|stringConstant
        
        myGenerate.commenceNonterminal("AssignmentStatement");

        //identifier
        acceptTerminal(Token.identifier);

        //:=
        acceptTerminal(Token.becomesSymbol);

        //<expression>|stringConstant -> check stringConstant first because it can be validated with a simple '=='
        if(nextToken.symbol == Token.stringConstant)
        {
            //stringConstant
            acceptTerminal(Token.stringConstant);
        }
        else
        {
            //<expression>
            try{expression();}
            catch(CompilationException ex)
            {throw new CompilationException(errorMessage("ExpressionStatement", nextToken), ex);}
        }

        myGenerate.finishNonterminal("AssignmentStatement");
    }

    private void ifStatement() throws CompilationException, IOException
    {
        //if <condition> then <statement list> end if | if <condition> then <statement list> else <statement list> end if

        myGenerate.commenceNonterminal("IfStatement");
        
        //if
        acceptTerminal(Token.ifSymbol);

        //<condition>
        try{condition();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("ConditionStatement", nextToken), ex);}

        //then
        acceptTerminal(Token.thenSymbol);

        //<statement list>
        try{statementList();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("StatementList", nextToken), ex);}

        //else
        acceptTerminal(Token.elseSymbol);

        //<statement list>
        try{statementList();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("StatementList", nextToken), ex);}

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
        try{condition();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("ConditionStatement", nextToken), ex);}

        //loop
        acceptTerminal(Token.loopSymbol);

        //<statement list>
        try{statementList();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("StatementList", nextToken), ex);}

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
        try{argumentList();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("ArgumentList", nextToken), ex);}

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
        try{statementList();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("StatementList", nextToken), ex);}

        //until
        acceptTerminal(Token.untilSymbol);

        //<condition>
        try{condition();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("Condition Statement", nextToken), ex);}

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
        try{assignmentStatement();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("AssignmentStatement", nextToken), ex);}

        //;
        acceptTerminal(Token.semicolonSymbol);

        //<condition>
        try{condition();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("ConditionStatement", nextToken), ex);}

        //;
        acceptTerminal(Token.semicolonSymbol);

        //<assignment statement>
        try{assignmentStatement();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("AssignmentStatement", nextToken), ex);}
        
        //)
        acceptTerminal(Token.rightParenthesis);

        //do
        acceptTerminal(Token.doSymbol);

        //<statement list>
        try{statementList();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("StatementList", nextToken), ex);}

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
        try{conditionalOperator();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("ConditionalOperator", nextToken), ex);}

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
        try{term();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("Term", nextToken), ex);}

        //+|-
        while(nextToken.symbol == Token.plusSymbol | nextToken.symbol == Token.minusSymbol)
        {
            //+|-
            acceptTerminal(nextToken.symbol);

            //<term>
            try{term();}
            catch(CompilationException ex)
            {throw new CompilationException(errorMessage("Term", nextToken), ex);}
        }

        myGenerate.finishNonterminal("Expression");
    }

    private void term() throws CompilationException, IOException
    {
        //<factor> | <term> * <factor> | <term> / <factor>
        //           <term> *|/ <factor>
        
        myGenerate.commenceNonterminal("Term");

        //<factor>
        try{factor();}
        catch(CompilationException ex)
        {throw new CompilationException(errorMessage("Factor", nextToken), ex);}

        //*|/
        while(nextToken.symbol == Token.timesSymbol | nextToken.symbol == Token.divideSymbol)
        {
            //*|/
            acceptTerminal(nextToken.symbol);

            //<factor
            try{factor();}
            catch(CompilationException ex)
            {throw new CompilationException(errorMessage("Factor", nextToken), ex);}
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
                myGenerate.reportError(nextToken, errorMessage("Identifier, Number, Expression", nextToken));
        }

        myGenerate.finishNonterminal("Factor");
    }
}
