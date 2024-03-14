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

    }

    @Override
    public void parse(PrintStream ps) throws IOException
    {
        myGenerate = new Generate();
        try
        {
            nextToken = lex.getNextToken();
            _statementPart_();
            acceptTerminal(Token.eofSymbol);
            myGenerate.reportSuccess();
        }
        catch (CompilationException ex)
        {
            ps.println("Compilation Exception");
            ps.println(ex.toTraceString());
        }
    }
}
