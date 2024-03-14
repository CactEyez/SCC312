// author Stephen Middlemass
import java.io.*;

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
    }

    private void statement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Statement");
    }

    private void assignmentStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("AssignmentStatement");
    }

    private void ifStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("IfStatement");
    }

    private void whileStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("WhileStatement");
    }

    private void procedureStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ProcedureStatement");
    }

    private void untilStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("UntilStatement");
    }

    private void forStatement() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ForStatement");
    }

    private void argumentList() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ArgumentList");
    }

    private void condition() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Condition");
    }

    private void conditionalOperator() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("ConditionalOperator");
    }

    private void expression() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Expressiono");
    }

    private void term() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Term");
    }

    private void factor() throws CompilationException, IOException
    {
        myGenerate.commenceNonterminal("Factor");
    }
}
