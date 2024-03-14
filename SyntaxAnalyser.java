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

        acceptTerminal(Token.endSymbol);
        acceptTerminal(Token.loopSymbol);

        myGenerate.finishNonterminal("IfStatement");
    }

    private void whileStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("WhileStatement");

        myGenerate.finishNonterminal("WhileStatement");
    }

    private void procedureStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ProcedureStatement");

        myGenerate.finishNonterminal("ProcedureStatement");
    }

    private void untilStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("UntilStatement");

        myGenerate.finishNonterminal("UntilStatement");
    }

    private void forStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ForStatement");

        myGenerate.finishNonterminal("ForStatement");
    }

    private void argumentList() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ArgumentList");

        myGenerate.finishNonterminal("ArgumentList");
    }

    private void condition() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Condition");

        myGenerate.finishNonterminal("Condition");
    }

    private void conditionalOperator() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ConditionalOperator");

        myGenerate.finishNonterminal("ConditionalOperator");
    }

    private void expression() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Expression");

        myGenerate.finishNonterminal("Expression");
    }

    private void term() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Term");

        myGenerate.finishNonterminal("Term");
    }

    private void factor() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Factor");

        myGenerate.finishNonterminal("Factor");
    }
}
