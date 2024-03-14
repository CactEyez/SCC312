public class Generate extends AbstractGenerate 
{
    @Override
    public void reportError(Token token, String explanatoryMessage) throws CompilationException
    {
        //explanatoryMessage is printed here to provide immediate feedback on an error
        System.out.println(explanatoryMessage);
        //By throwing a CompilationException aswell it also provides more structured error handling and reporting
        throw new CompilationException(explanatoryMessage);
    }
}
