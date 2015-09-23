package net.lldp.checksims.algorithm.syntaxtree;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.java.Java8BaseVisitor;
import net.lldp.checksims.parse.ast.java.Java8Parser.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

public class FullyImplementedTreeWalker extends Java8BaseVisitor<AST> 
{
    private final static Map<String, Method> myMethods = new HashMap<>();
    private final static Map<String, AST> names = new HashMap<>();
    static
    {
        for(Method m : Java8BaseVisitor.class.getMethods())
        {
            myMethods.put(m.getName(), m);
        }
    }
    
    @Override
    public AST visitChildren(RuleNode rn)
    {
        String callerName = Thread.currentThread().getStackTrace()[3].getMethodName();
        Class<?> paramType = myMethods.get(callerName).getParameters()[0].getType();
        System.out.println("@Override\npublic AST " + callerName + "(" + paramType.getSimpleName()+" ctx)\n{\n" + s + "}\n");
        return super.visitChildren(rn);
    }
    
    @Override
    public AST visitClassDeclaration(ClassDeclarationContext ctx)
    {
        for(ParseTree pt : ctx.children)
        {
            switch (pt.getClass().getSimpleName())
            {
                case "NormalClassDeclarationContext":
                    return pt.accept(this);
            }
        }
        
        throw new RuntimeException("NULL");
    }
    
    @Override
    public AST visitNormalClassDeclaration(NormalClassDeclarationContext ctx)
    {
        int TOC = ctx.getChildCount() - 2;
        
        String name = ctx.children.get(TOC).getText(); // name
        AST t = ctx.children.get(TOC+1).accept(this); // body
        
        names.put(name, t);
        return t;
    }
    
    @Override
    public AST visitClassBody(ClassBodyContext ctx)
    {
        List<AST> t = new LinkedList<>();
        for(ParseTree pt : ctx.children)
        {
            switch (pt.getClass().getSimpleName())
            {
                case "ClassBodyDeclarationContext":
                    AST y = pt.accept(this);
                    if (y != null)
                    {
                        t.add(y);
                    }
            }
        }
        
        return new AST.UnorderedAST(t.stream());
    }
    
    @Override
    public AST visitClassBodyDeclaration(ClassBodyDeclarationContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitClassMemberDeclaration(ClassMemberDeclarationContext ctx)
    {
        for(ParseTree pt : ctx.children)
        {
            //System.out.println(pt.getClass().getSimpleName());
            //System.out.println(pt.getText());
            //System.out.println();
            switch(pt.getClass().getSimpleName())
            {
                case "FieldDeclarationContext":
                case "MethodDeclarationContext":
                case "ClassDeclarationContext":
                //case "InterfaceDeclarationContext":
                    return pt.accept(this);
            }
            
            System.out.println(pt.getClass().getSimpleName());
        }
        
        return null;
    }
    
    @Override
    public AST visitMethodDeclaration(MethodDeclarationContext ctx)
    {
        List<AST> t = new LinkedList<>();
        
        for(ParseTree pt : ctx.children)
        {
            switch(pt.getClass().getSimpleName())
            {
                case "MethodBodyContext":
                case "MethodHeaderContext":
                    t.add(pt.accept(this));
            }
        }
        
        return new AST.OrderedAST(t.stream());
    }
    
    @Override
    public AST visitMethodHeader(MethodHeaderContext ctx)
    {
        List<AST> t = new LinkedList<>();
        for(ParseTree pt : ctx.children)
        {
            switch(pt.getClass().getSimpleName())
            {
                case "ResultContext":
                    t.add(new AST.NodeAST(pt.getText())); break;
                case "MethodDeclaratorContext":
                    t.add(pt.accept(this)); break;
            }
        }
        
        return new AST.OrderedAST(t.stream());
    }
    
    @Override
    public AST visitMethodDeclarator(MethodDeclaratorContext ctx)
    {
        List<AST> t = new LinkedList<>();
        
        t.add(new AST.NodeAST(ctx.children.get(0).getText()));
        t.add(ctx.children.get(2).accept(this));
        
        return new AST.OrderedAST(t.stream());
    }
    
    @Override
    public AST visitFormalParameterList(FormalParameterListContext ctx)
    {
        List<AST> t = new LinkedList<>();
        for(ParseTree pt : ctx.children)
        {
            switch(pt.getClass().getSimpleName())
            {
                case "LastFormalParameterContext":
                    t.add(pt.accept(this));
                    break;
                case "FormalParametersContext":
                    AST.OrderedAST res =  (AST.OrderedAST) pt.accept(this);
                    t.addAll(res.getBody());
                    break;
            }
        }
        
        return new AST.OrderedAST(t.stream());
    }
    
    
    @Override
    public AST visitLastFormalParameter(LastFormalParameterContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitFormalParameters(FormalParametersContext ctx)
    {
        List<AST> t = new LinkedList<>();
        
        t.add(ctx.children.get(0).accept(this));
        t.add(ctx.children.get(2).accept(this));
        
        return new AST.OrderedAST(t.stream());
    }
    
    @Override
    public AST visitFormalParameter(FormalParameterContext ctx)
    {
        return new AST.NodeAST(ctx.children.get(0).getText());
    }
    
    @Override
    public AST visitMethodBody(MethodBodyContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitBlock(BlockContext ctx)
    {
        return ctx.children.get(1).accept(this);
    }
    
    @Override
    public AST visitBlockStatements(BlockStatementsContext ctx)
    {
        List<AST> t = new LinkedList<>();
        for(ParseTree pt : ctx.children)
        {
            t.add(pt.accept(this));
        }
        return new AST.UnorderedAST(t.stream());
    }
    
    @Override
    public AST visitBlockStatement(BlockStatementContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitStatement(StatementContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitStatementWithoutTrailingSubstatement(StatementWithoutTrailingSubstatementContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitExpressionStatement(ExpressionStatementContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitStatementExpression(StatementExpressionContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitMethodInvocation(MethodInvocationContext ctx)
    {
        List<AST> t = new LinkedList<>();
        switch(ctx.children.get(0).getClass().getSimpleName())
        {
            case "TypeNameContext": // TODO there is another case! read the g4 file, line 1104
                t.add(new AST.NodeAST(ctx.children.get(0).getText() + ctx.children.get(2).getText()));
                t.add(ctx.children.get(4).accept(this));
                break;
            case "MethodNameContext":
                t.add(new AST.NodeAST(ctx.children.get(0).getText()));
                t.add(ctx.children.get(2).accept(this));
                break;
            case "ExpressionNameContext":
        }
        return new AST.OrderedAST(t.stream());
    }
    
    @Override
    public AST visitArgumentList(ArgumentListContext ctx)
    {
        List<AST> t = new LinkedList<>();
        for(ParseTree pt : ctx.children)
        {
            switch(pt.getClass().getSimpleName())
            {
                case "ExpressionContext":
                    t.add(pt.accept(this));
            }
        }
        return new AST.OrderedAST(t.stream());
    }
    
    @Override
    public AST visitExpression(ExpressionContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitAssignmentExpression(AssignmentExpressionContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitConditionalExpression(ConditionalExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            //TODO ternary expression
            throw new RuntimeException("NULL");
        }
    }
    
    @Override
    public AST visitConditionalOrExpression(ConditionalOrExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
        }
    }
    
    @Override
    public AST visitConditionalAndExpression(ConditionalAndExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
        }
    }
    
    @Override
    public AST visitInclusiveOrExpression(InclusiveOrExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
        }
    }
    
    @Override
    public AST visitExclusiveOrExpression(ExclusiveOrExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
        }
    }
    
    @Override
    public AST visitAndExpression(AndExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
        }
    }
    
    @Override
    public AST visitEqualityExpression(EqualityExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            switch(ctx.children.get(1).getText())
            {
                case "==": return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
                case "!=": return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
            }
        }
        throw new RuntimeException(ctx.children.get(1).getText());
    }
    
    @Override
    public AST visitRelationalExpression(RelationalExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            return new AST.OrderedAST(op, new AST.OrderedAST(left, right));
        }
    }
    
    @Override
    public AST visitShiftExpression(ShiftExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            boolean trip = ctx.children.size() == 5;
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText() + 
                                 ctx.getChild(2).getText() +
                                 (trip ? ctx.getChild(3).getText() : ""));
            AST right = ctx.children.get(trip ? 4 : 3).accept(this);
            
            return new AST.OrderedAST(op, new AST.OrderedAST(left, right));
        }
    }
    
    @Override
    public AST visitAdditiveExpression(AdditiveExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST left = ctx.children.get(0).accept(this);
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST right = ctx.children.get(2).accept(this);
            
            switch(ctx.children.get(1).getText())
            {
                case "+": return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
                case "-": return new AST.OrderedAST(op, new AST.OrderedAST(left, right));
            }
        }
        throw new RuntimeException(ctx.children.get(1).getText());
    }
    
    @Override
    public AST visitMultiplicativeExpression(MultiplicativeExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            AST op = new AST.NodeAST(ctx.getChild(1).getText());
            AST left = ctx.children.get(0).accept(this);
            AST right = ctx.children.get(2).accept(this);
            
            switch(ctx.children.get(1).getText())
            {
                case "*": return new AST.OrderedAST(op, new AST.UnorderedAST(left, right));
                case "/": return new AST.OrderedAST(op, new AST.OrderedAST(left, right));
                case "%": return new AST.OrderedAST(op, new AST.OrderedAST(left, right));
            }
        }
        throw new RuntimeException(ctx.children.get(1).getText());
    }
    
    @Override
    public AST visitUnaryExpression(UnaryExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            throw new RuntimeException("NULL");
        }
    }
    
    @Override
    public AST visitUnaryExpressionNotPlusMinus(UnaryExpressionNotPlusMinusContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            //TODO + - expression
            throw new RuntimeException("NULL");
        }
    }
    
    @Override
    public AST visitPostfixExpression(PostfixExpressionContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            //TODO not sure?
            throw new RuntimeException("NULL");
        }
    }
    
    @Override
    public AST visitPrimary(PrimaryContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            //TODO primaryNoNewArray_lf_primary / arrayCreationExpression
            throw new RuntimeException("NULL");
        }
    }
    
    @Override
    public AST visitPrimaryNoNewArray_lfno_primary(PrimaryNoNewArray_lfno_primaryContext ctx)
    {
        if (ctx.children.size() == 1)
        {
            return ctx.children.get(0).accept(this);
        }
        else
        {
            //TODO see java8.g4 line 1013
            throw new RuntimeException("NULL");
        }
    }
    
    @Override
    public AST visitLiteral(LiteralContext ctx)
    {
        return new AST.NodeAST(ctx.getText());
    }
    
    @Override
    public AST visitExpressionName(ExpressionNameContext ctx)
    {
        if (ctx.getChild(0).getClass().getSimpleName().equals("TerminalNodeImpl"))
        {
            return new AST.NodeAST(ctx.getText());
        }

        throw new RuntimeException("NULL");
    }
    
    public AST visitLocalVariableDeclarationStatement(LocalVariableDeclarationStatementContext ctx)
    {
        return ctx.children.get(0).accept(this);
    }
    
    @Override
    public AST visitVariableDeclaratorList(VariableDeclaratorListContext ctx)
    {
        List<AST> result = new LinkedList<AST>();
        for(ParseTree pt : ctx.children)
        {
            if (pt.getClass().getSimpleName().equals("VariableDeclaratorContext"))
            {
                result.add(pt.accept(this));
            }
        }
        return new AST.OrderedAST(result.stream());
    }
    
    @Override
    public AST visitLocalVariableDeclaration(LocalVariableDeclarationContext ctx)
    {
        List<AST> result = new LinkedList<AST>();
        
        for(ParseTree pt : ctx.children)
        {
            switch(pt.getClass().getSimpleName())
            {
                case "UnannTypeContext":
                    result.add(new AST.NodeAST(pt.getText()));
                    break;
                case "VariableDeclaratorListContext":
                    result.addAll(((AST.OrderedAST)pt.accept(this)).getBody());
                    break;
            }
        }
        return new AST.OrderedAST(result.stream());
    }
    
    @Override
    public AST visitVariableDeclarator(VariableDeclaratorContext ctx)
    {
        return ctx.getChild(2).accept(this);
    }
    
    @Override
    public AST visitVariableInitializer(VariableInitializerContext ctx)
    {
        return ctx.getChild(0).accept(this);
    }
    
    @Override
    public AST visitFieldDeclaration(FieldDeclarationContext ctx)
    {
        List<AST> result = new LinkedList<AST>();
        
        for(ParseTree pt : ctx.children)
        {
            switch(pt.getClass().getSimpleName())
            {
                case "UnannTypeContext":
                    result.add(new AST.NodeAST(pt.getText()));
                    break;
                case "VariableDeclaratorListContext":
                    result.addAll(((AST.OrderedAST)pt.accept(this)).getBody());
                    break;
            }
        }
        return new AST.OrderedAST(result.stream());
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    String s = 
    "    for(ParseTree pt : ctx.children)\n" +
    "    {\n" +
    "        System.out.println(ctx.getClass().getSimpleName());\n"+
    "        System.out.println(pt.getClass().getSimpleName());\n" +
    "        System.out.println(pt.getText());\n" +
    "        System.out.println();\n" +
    "    }\n" +
    "    return null;\n";
    
}