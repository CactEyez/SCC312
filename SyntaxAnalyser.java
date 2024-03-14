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
}
