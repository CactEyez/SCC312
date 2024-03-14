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

    }

    private void statement() throws CompilationException, IOException
    {
        
    }

    private void assignment() throws CompilationException, IOException
    {
        
    }

    private void ifStructure() throws CompilationException, IOException
    {
        
    }

    private void whileStructure() throws CompilationException, IOException
    {
        
    }

    private void procedureStructure() throws CompilationException, IOException
    {
        
    }

    private void untilStructure() throws CompilationException, IOException
    {
        
    }

    private void forStructure() throws CompilationException, IOException
    {
        
    }

    private void argumentList() throws CompilationException, IOException
    {
        
    }

    private void condition() throws CompilationException, IOException
    {
        
    }

    private void conditionalOperator() throws CompilationException, IOException
    {
        
    }

    private void expression() throws CompilationException, IOException
    {
        
    }

    private void term() throws CompilationException, IOException
    {
        
    }

    private void factor() throws CompilationException, IOException
    {
        
    }
}
