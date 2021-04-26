package parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.crypto.tink.subtle.Kwp;

import evaluator.Evaluator;
import libs.FileImporter;
import libs.LibLoader;
import main.EntryPoint;
import main.Token;
import main.TokenType;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.ListSetterNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import parser.nodes.builders.ArrayBuilder;
import parser.nodes.builders.DictBuilder;
import parser.nodes.builders.FunctionBuilder;
import parser.nodes.getters.FuncGetterNode;
import parser.nodes.getters.GetterNode;
import parser.nodes.getters.IteratorGetterNode;
import parser.nodes.getters.ListGetterNode;
import parser.nodes.getters.ReturnGetterNode;
import parser.nodes.innerreturn.BreakNode;
import parser.nodes.innerreturn.ContinueNode;
import parser.nodes.statements.ForNode;
import parser.nodes.statements.GlobalizerNode;
import parser.nodes.statements.IfNode;
import variables.ClassNode;
import variables.VariableContext;

public class Parser {
	
	private List<Token> tokens;
	private int tok_id = 0;
	private int length;
	public Token current_token;
	public boolean advanceResult;
	public VariableContext saverContext = EntryPoint.globalContext;
	
	public Token advance() {
		this.tok_id += 1;
		this.advanceResult = false;
		if (this.tok_id < this.length) {
			this.advanceResult = true;
			this.current_token = this.tokens.get(this.tok_id);
		}
		return this.current_token;
	}
	public Token next() {
		if (this.tok_id + 1 < this.tokens.size()) {
			return this.tokens.get(this.tok_id + 1);
		}
		return null;
	}
	
	public Token last() {
		if (this.tok_id > 0 && this.tok_id <= this.tokens.size()) {
			return this.tokens.get(this.tok_id - 1);
		}
		return null;
	}
	
	private int iModifier = -1;
	public ArrayList<Node> parse(List<Token> tokens, TokenType end) {
		ArrayList<Node> parentNodes = new ArrayList<>();
		
		parentNodes.add(this.parseToken(tokens, false));
		int last_eof_id = this.tok_id;
		// IModify
		if (this.current_token.type == end) {
			return parentNodes;
		}
		if (this.current_token.type != TokenType.EOF) {
			System.out.println("Syntax Error, Expression invalid");
			EntryPoint.raiseToken(tokens.get(last_eof_id));
			return null; 
		}
		if (this.next() != null && this.next().type == end) {
			return parentNodes;
		}
		
		for(int i = last_eof_id + 1; i < tokens.size(); i++) {
			if (tokens.get(i).type == end) {
				this.tok_id = i - 1;
				this.advance();
				return parentNodes;
			}
			
			if (tokens.get(i).type == TokenType.EOF) {
				this.tok_id = last_eof_id;
				this.advance();
				if(this.current_token.type == TokenType.EOF) {
					last_eof_id += 1;
					continue;
				}
				
				iModifier = -1;
				parentNodes.add(this.parseToken(tokens, last_eof_id + 1, i));
				this.length = this.tokens.size();
				if(iModifier != -1) {
					i = iModifier;
				}
				last_eof_id = i;
				if (tokens.get(this.tok_id).type != TokenType.EOF) {
					System.out.println("Syntax Error, Line invalid");
					EntryPoint.raiseToken(tokens.get(this.tok_id));
					return null;
				}
				if (this.next() != null && this.next().type == end) {
					return parentNodes;
				}
			}
		}
		
		return parentNodes;
	}
	
	private Node parseToken(List<Token> tokens, int begin, int length) {
		this.tokens = tokens;
		this.length = this.tokens.size();
		this.tok_id = begin - 1;
		this.length = length;
		this.advance();
		return parseChoice();
	}
	
	private Node parseChoice() {
		int cp_tok_id = this.tok_id;
		if (this.current_token.type == TokenType.GLOBAL) {			
			this.advance();
			Node n = this.parseChoice();
			if (n instanceof SetterNode) {
				((SetterNode) n).isGlobalSetted = true;
				return n;
			}
			if(n instanceof ListSetterNode) {
				((ListSetterNode) n).isGlobalContext = true;
				return n;
			}
			this.tok_id = cp_tok_id - 1;
			this.advance();
		}
		
		if (this.current_token.type == TokenType.EOF) {
			return null;
		}
		if(this.current_token.type == TokenType.RETURN) {
			return this.parseReturn();
		}
		if(this.current_token.type == TokenType.BREAK) {
			return this.parseBreak();
		}
		if(this.current_token.type == TokenType.CONTINUE) {
			return this.parseContinue();
		}
		if (this.current_token.type == TokenType.FUNCTION) {
			return this.parseFunction();
		}
		if (this.current_token.type == TokenType.IF) {
			return this.parseIf();
		}
		if (this.current_token.type == TokenType.FOR) {
			return this.parseFor();
		}
		if (this.current_token.type == TokenType.CLASS) {
			return this.parseClass();
		}
		if (this.current_token.type == TokenType.IMPORT) {
			return this.parseImport();
		}
		if (this.current_token.type == TokenType.FROM) {
			return this.parseFromImport();
		}
		return this.parseNode();
	}
	
	private Node parseImport() {
		this.advance();
		if (this.current_token.type != TokenType.NAME) {
			System.out.println("Missing name after import");
			EntryPoint.raiseToken(this.current_token);
			return null;
		}

		if (!LibLoader.loadModule((String) this.current_token.value)) {
			ClassNode cs = FileImporter.importFile((String) this.current_token.value, this.current_token);
			
			String[] spl = ((String) this.current_token.value).split("\\.");
			cs.name = spl[spl.length - 1];
			
			if (cs != null) {
				EntryPoint.globalContext.createClasses((String) this.current_token.value, cs);
				EntryPoint.globalContext.setValue((String) this.current_token.value, cs);
			}
		}
		this.advance();
		
		return null;
	}
	
	private Node parseFromImport() {
		this.advance();
		if (this.current_token.type != TokenType.NAME) {
			System.out.println("Missing name after import");
			EntryPoint.raiseToken(this.current_token);
			return null;
		}

		ClassNode cs = null;
		if (!LibLoader.hasModule((String) this.current_token.value)) {
			cs = FileImporter.importFile((String) this.current_token.value, this.current_token);
		} else {
			cs = LibLoader.getModule((String)this.current_token.value);
		}
		this.advance();
		
		if (this.current_token.type != TokenType.IMPORT) {
			System.out.println("Missing import after from name");
			EntryPoint.raiseToken(this.current_token);
			return null;
		}
		this.advance();
		
		ArrayList<String> datas = new ArrayList<String>();
		if (this.current_token.type == TokenType.MUL) {
			datas.add("*");
			this.advance();
		} else if (this.current_token.type == TokenType.NAME){
			while(this.current_token.type == TokenType.NAME){
				datas.add((String) this.current_token.value);
				this.advance();
				if (this.current_token.type != TokenType.COMMA) {
					break;
				}
				this.advance();
			}
		}
		
		EntryPoint.globalContext.importClass(cs, datas);
		
		return null;
	}
	
	private Node parseClass() {
		Token t = this.current_token;
		this.advance();
		if (this.current_token.type == TokenType.NAME) {
			String name = (String) this.current_token.value;
			this.advance();
			if (this.current_token.type != TokenType.LCURLYBRACKET) {
				System.out.println("Missing left curly bracket.");
				EntryPoint.raiseToken(this.current_token);
				return null;
			}
			this.advance();
			
			VariableContext ctx = new VariableContext();
			
			Parser p = new Parser();
			p.tokens = this.tokens;
			p.tok_id = this.tok_id - 1;
			p.length = this.tokens.size();
			p.saverContext = ctx;
			p.advance();
			ArrayList<Node> nodes = p.parse(this.tokens, TokenType.RCURLYBRACKET);
			
			this.tok_id = p.tok_id - 1;
			this.length = this.tokens.size();
			this.advance();
			
			if (this.current_token.type != TokenType.RCURLYBRACKET) {
				this.advance();
				if (this.current_token.type != TokenType.RCURLYBRACKET) {
					System.out.println("Missing right curly bracket.");
					EntryPoint.raiseToken(this.current_token);
					return null;
				}
			}
			this.advance();
			iModifier = this.tok_id;
			
			EntryPoint.registerStack(ctx);
			EntryPoint.setStackName("<classloader>");
			Evaluator.evaluate(nodes, ctx, false);
			EntryPoint.unregisterStack(ctx);
			
			ClassNode clNode = new ClassNode(t.col, t.line);
			clNode.name = name;
			clNode.importContext(ctx);
			
			saverContext.setValue(name, clNode);
		} else {
			System.out.println("Expected name, got "+this.current_token.type);
			EntryPoint.raiseToken(this.current_token);
		}
		return null;
	}
	private Node parseBreak() {
		Token t = this.current_token;
		this.advance();
		int i = 1;
		if(this.current_token.type == TokenType.LPAREN) {
			this.advance();
			Node ex = this.expr();
			try {
				Object v = ex.evaluate(null);
				if (v instanceof NumberNode && ((NumberNode)v).isInt() && ((NumberNode)v).isIntegerRange()) {
					i = ((NumberNode)v).getNumber().intValue();
				}else {
					System.out.println("Break only supports integers < 2^31");
					EntryPoint.raiseToken(t);
					return null;
				}
			} catch(Exception e) {
				System.out.println("Break does not support dynamic expression");
				EntryPoint.raiseToken(t);
				return null;
			}
			if (this.current_token.type != TokenType.RPAREN) {
				System.out.println("Missing right parenthesies of break");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
		}
		
		if (i<=0) {
			System.out.println("Break only supports positive integers");
			EntryPoint.raiseToken(t);
			return null;
		}
		
		return new BreakNode(t.col, t.line, i);
	}
	
	private Node parseContinue() {
		Token t = this.current_token;
		this.advance();
		int i = 1;
		int j = 1;
		if(this.current_token.type == TokenType.LPAREN) {
			this.advance();
			Node ex = this.expr();
			try {
				Object v = ex.evaluate(null);
				if (v instanceof NumberNode && ((NumberNode)v).isInt() && ((NumberNode)v).isIntegerRange()) {
					i = ((NumberNode)v).getNumber().intValue();
				}else {
					System.out.println("Continue count only supports integers < 2^31");
					EntryPoint.raiseToken(t);
					return null;
				}
			} catch(Exception e) {
				System.out.println("Continue count does not support dynamic expression");
				EntryPoint.raiseToken(t);
				return null;
			}
			if (this.current_token.type == TokenType.COMMA) {
				this.advance();
				Node ex2 = this.expr();
				try {
					Object v = ex2.evaluate(null);
					if (v instanceof NumberNode && ((NumberNode)v).isInt() && ((NumberNode)v).isIntegerRange()) {
						j = ((NumberNode)v).getNumber().intValue();
					}else {
						System.out.println("Continue jump count only supports integers");
						EntryPoint.raiseToken(t);
						return null;
					}
				} catch(Exception e) {
					System.out.println("Continue jump count does not support dynamic expression");
					EntryPoint.raiseToken(t);
					return null;
				}
				
			}
			
			if (this.current_token.type != TokenType.RPAREN) {
				System.out.println("Missing right parenthesies of continue");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
		}
		
		if (i<=0) {
			System.out.println("Continue count only supports positive integers");
			EntryPoint.raiseToken(t);
			return null;
		}
		if (j<=0) {
			System.out.println("Continue jump count only supports positive integers");
			EntryPoint.raiseToken(t);
			return null;
		}
		
		return new ContinueNode(t.col, t.line, i, j);
	}
	
	public Node parseReturn() {
		Token t = this.current_token;
		this.advance();
		Node dat = this.parseToken(tokens, this.tok_id, this.length);
		return new ReturnGetterNode(t.col, t.line, dat);
	}
	
	private Node parseFunction() {
		this.advance();
		
		if(this.current_token.type == TokenType.NAME) {
			String expectedReturnType = "";
			if (this.next() != null && this.next().type == TokenType.NAME) {
				expectedReturnType = (String) this.current_token.value;
				this.advance();
			}
			
			String name = (String) this.current_token.value;
			
			this.advance();
			if(this.current_token.type != TokenType.LPAREN) {
				return null;
			}
			this.advance();
			
			boolean agEnabled = false;
			String agName = "";
			boolean kwEnabled = false;
			String kwName = "";
			
			ArrayList<StringNode> args = new ArrayList<>();
			HashMap<StringNode, Node> kwargs = new HashMap<StringNode,Node>();
			HashMap<String, String> expectedTypeVar = new HashMap();
			TokenType lastType = this.current_token.type;
			while(this.advanceResult && lastType != TokenType.RPAREN
					&& (this.current_token.type == TokenType.NAME
					|| this.current_token.type == TokenType.MUL)) {
				if (this.current_token.type == TokenType.MUL) {
					this.advance();
					if (this.advanceResult && this.current_token.type == TokenType.MUL
							&& this.next() != null
							&& this.next().type == TokenType.NAME) {
						this.advance();
						kwEnabled = true;
						kwName = (String) this.current_token.value;
					} else if (this.current_token.type == TokenType.NAME) {
						agEnabled = true;
						agName = (String) this.current_token.value;
					} else {
						System.out.println("Unexpected Args/Kwargs token (*)");
						EntryPoint.raiseToken(this.current_token);
					}
					this.advance();
					lastType = this.current_token.type;
					if (lastType != TokenType.RPAREN) {
						this.advance();
					}
					continue;
				}
				
				Token a = this.current_token;
				String typeVarName = null;
				this.advance();
				
				if (this.current_token.type == TokenType.TWO_POINTS) {
					this.advance();
					
					if (this.current_token.type != TokenType.NAME) {
						System.out.println("Expected variable type container after two points");
						EntryPoint.raiseToken(this.current_token);
					}
					
					typeVarName = (String) this.current_token.value;
					this.advance();
				}
				
				lastType = this.current_token.type;
				
				if (lastType == TokenType.SET) {
					this.advance();
					
					Node exp = this.bin();
				
					lastType = this.current_token.type;
					
					kwargs.put(new StringNode(a.col, a.line, (String) a.value),exp);
				}
				args.add(new StringNode(a.col, a.line, (String) a.value));
				if(typeVarName != null) {
					expectedTypeVar.put((String) a.value, typeVarName);
				}
				
				if(lastType != TokenType.RPAREN) {
					this.advance();
				}
			}
			
			if(this.current_token.type != TokenType.RPAREN) {
				return null;
			}
			this.advance();
			if(this.current_token.type != TokenType.LCURLYBRACKET) {
				return null;
			}
			this.advance();
			
			Parser p = new Parser();
			p.tokens = this.tokens;
			p.tok_id = this.tok_id - 1;
			p.length = this.tokens.size();
			p.advance();
			ArrayList<Node> nodes = p.parse(this.tokens, TokenType.RCURLYBRACKET);
			
			/*FunctionNode n = new FunctionNode(0, 0);
			n.evaluators = nodes;
			n.arguments = args;
			n.kwargs = kwargs;
			n.name = name;*/
			
			FunctionBuilder fb = new FunctionBuilder(0,0, args, kwargs, name, nodes, agEnabled, agName, kwEnabled, kwName, expectedTypeVar, expectedReturnType);
			
			if (p.current_token.type != TokenType.RCURLYBRACKET) {
				p.advance();
				if (p.current_token.type != TokenType.RCURLYBRACKET) {
					return null;
				}
			}
			
			p.advance();
			this.iModifier = p.tok_id;
			this.tok_id = p.tok_id - 1;
			this.advance();
			return fb;
		}
		
		return null;
	}
	private Node parseIf() {
		IfNode fdat = null;
		IfNode adat = null;
		boolean hasElse = false;
		this.length = tokens.size();
		while(this.current_token.type == TokenType.IF) {
			hasElse = false;
			this.advance();
	
			Token t = this.current_token;
			if(this.current_token.type == TokenType.LPAREN) {
				Node expr = this.bin();
				this.tok_id -= 2;
				this.advance();
				if(this.current_token.type != TokenType.RPAREN) {
					System.out.println("Syntax error, missing right parenthesies");
					EntryPoint.raiseToken(t);
					return null;
				}
				
				this.advance();
				if(this.current_token.type != TokenType.LCURLYBRACKET) {
					System.out.println("Syntax error, missing left curly bracket \"{\"");
					EntryPoint.raiseToken(t);
					return null;
				}
				this.advance();
				
				Parser p = new Parser();
				p.tokens = this.tokens;
				p.tok_id = this.tok_id - 1;
				p.length = this.tokens.size();
				p.advance();
				ArrayList<Node> nodes = p.parse(this.tokens, TokenType.RCURLYBRACKET);
				
				FunctionNode n = new FunctionNode(0, 0);
				n.evaluators = nodes;
				n.arguments = new ArrayList<>();
				
				if (p.current_token.type != TokenType.RCURLYBRACKET) {
					p.advance();
					if (p.current_token.type != TokenType.RCURLYBRACKET) {
						return null;
					}
				}
				p.advance();
				
				this.iModifier = p.tok_id;
				this.tok_id = p.tok_id - 1;
				this.advance();
				
				IfNode dat = new IfNode(t.col, t.line, n, expr);
				
				if (fdat == null) {
					fdat = dat;
					adat = dat;
				} else {
					adat.elser = dat;
					adat = dat;
				}
			} else {
				System.out.println("Missing left parenthesies");
				EntryPoint.raiseToken(current_token);
				return null;
			}
			
			if (this.current_token.type == TokenType.ELSE ||
					(this.next() != null && this.next().type == TokenType.ELSE)) {
				if (this.current_token.type != TokenType.ELSE) {
					this.advance();
				}
				hasElse = true;
				this.advance();
			} else {
				return fdat;
			}
		}
		
		if (this.current_token.type == TokenType.LCURLYBRACKET) {
			this.advance();
			
			Parser p = new Parser();
			p.tokens = this.tokens;
			p.tok_id = this.tok_id - 1;
			p.length = this.tokens.size();
			p.advance();
			ArrayList<Node> nodes = p.parse(this.tokens, TokenType.RCURLYBRACKET);
			
			FunctionNode n = new FunctionNode(0, 0);
			n.evaluators = nodes;
			n.arguments = new ArrayList<>();
			
			if (p.current_token.type != TokenType.RCURLYBRACKET) {
				p.advance();
				if (p.current_token.type != TokenType.RCURLYBRACKET) {
					return null;
				}
			}
			p.advance();
			
			this.iModifier = p.tok_id;
			this.tok_id = p.tok_id - 1;
			this.advance();
			
			adat.elser = n;
		} else {
			System.out.println("Missing left curly bracket");
			EntryPoint.raiseToken(current_token);
			return null;
		}
		
		return fdat;
	}
	
	private Node parseFor() {
		int cp_tok_id = this.tok_id;
		Token t = this.current_token;
		this.advance();
		
		if(this.current_token.type == TokenType.LPAREN) {
			this.advance();
			if(this.current_token.type == TokenType.NAME) {
				String name = (String) this.current_token.value;
				this.advance();
				if (this.current_token.type == TokenType.TWO_POINTS) {
					this.advance();
					
					Node left = this.parseToken(this.tokens, this.tok_id, this.tokens.size());
					
					if(this.current_token.type == TokenType.RPAREN) {
						
						this.advance();
						if(this.current_token.type == TokenType.LCURLYBRACKET) {
							this.advance();
							
							Parser p = new Parser();
							p.tokens = this.tokens;
							p.tok_id = this.tok_id - 1;
							p.length = this.tokens.size();
							p.advance();
							ArrayList<Node> nodes = p.parse(this.tokens, TokenType.RCURLYBRACKET);
							
							FunctionNode n = new FunctionNode(0, 0);
							n.evaluators = nodes;
							n.arguments = new ArrayList<>();
							
							if (p.current_token.type != TokenType.RCURLYBRACKET) {p.advance();}
							
							if (p.current_token.type == TokenType.RCURLYBRACKET) {
								p.advance();
								
								this.iModifier = p.tok_id;
								this.tok_id = p.tok_id - 1;
								this.advance();
								
								return new ForNode(t.col, t.line, n, new IteratorGetterNode(t.col, t.line, left), name);
							}
						}
					}
				}
			}
		}
		
		this.tok_id = cp_tok_id;
		this.advance();

		if(this.current_token.type == TokenType.LPAREN) {
			this.advance();
			Node exprSet = this.parseToken(this.tokens, this.tok_id, this.tokens.size());
			if(this.current_token.type != TokenType.EOF) {
				System.out.println("Syntax error, missing ;");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
			Node exprComp = this.bin();
			if(this.current_token.type != TokenType.EOF) {
				System.out.println("Syntax error, missing ;");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
			Node exprAdv = this.parseToken(this.tokens, this.tok_id, this.tokens.size());
			
			if(this.current_token.type != TokenType.RPAREN) {
				System.out.println("Syntax error, missing right parenthesies");
				EntryPoint.raiseToken(t);
				return null;
			}
			
			this.advance();
			if(this.current_token.type != TokenType.LCURLYBRACKET) {
				System.out.println("Syntax error, missing left curly bracket \"{\"");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
			
			Parser p = new Parser();
			p.tokens = this.tokens;
			p.tok_id = this.tok_id - 1;
			p.length = this.tokens.size();
			p.advance();
			ArrayList<Node> nodes = p.parse(this.tokens, TokenType.RCURLYBRACKET);
			
			FunctionNode n = new FunctionNode(0, 0);
			n.evaluators = nodes;
			n.arguments = new ArrayList<>();
			
			if (p.current_token.type != TokenType.RCURLYBRACKET) {
				p.advance();
				if (p.current_token.type != TokenType.RCURLYBRACKET) {
					return null;
				}
			}
			p.advance();
			
			this.iModifier = p.tok_id;
			this.tok_id = p.tok_id - 1;
			this.advance();
			
			return new ForNode(t.col, t.line, n, exprSet, exprComp, exprAdv);
		}
		
		return null;
	}
	
	private Node parseToken(List<Token> tokens, boolean reset) {
		this.tokens = tokens;
		this.length = this.tokens.size();
		if (reset) {
			this.tok_id = -1;
			this.advance();
		} else {
			this.tok_id -= 1;
			this.advance();
		}
		return parseChoice();
	}
	
	private boolean contains(TokenType ty, TokenType[] types) {
		for (TokenType t:types) {
			if (ty == t) {
				return true;
			}
		}
		return false;
	}
	
	private Node parseNode() {
		int cp_tok_id = this.tok_id;
		Token cur_tok = this.current_token;
		
		if(this.current_token.type == TokenType.NAME) {
			this.advance();
			
			if(this.current_token.type == TokenType.SET) {
				this.advance();
				Node n = this.bin();
				if (n == null) {
					return null;
				}
				return new SetterNode(n, (String) cur_tok.value, cur_tok.col, cur_tok.line);
			} else if(this.current_token.type == TokenType.LHOOK) {
				Node listSet = this.listSetExpr(new GetterNode((String) cur_tok.value, cur_tok.col, cur_tok.line));
				if (this.current_token.type == TokenType.SET) {
					this.advance();
					Node n = this.bin();
					if (n == null) {
						return null;
					}
					if (listSet instanceof ListSetterNode) {
						
						ListSetterNode dat = ((ListSetterNode)listSet);
						dat.expr = n;
						return dat;
						
					} else {
						return null;
					}
				}
			}
			

			this.tok_id = cp_tok_id - 1;
			this.advance();
			this.advance();
			TokenType[] opeq_types = new TokenType[] {
					TokenType.PLUS, TokenType.MINUS,
					TokenType.POW, TokenType.MUL, TokenType.DIV,
			};
			int opeq_type_id = -1;
			int i = 0;
			for (TokenType t:opeq_types) {
				if (t == this.current_token.type) {
					opeq_type_id = i;
					break;
				}
				i++;
			}
			
			if (opeq_type_id != -1) {
				this.advance();
				if (this.current_token.type == TokenType.SET) {
					this.advance();
					Node n = this.bin();
					if (n == null) {
						return null;
					}
					Node g = new GetterNode((String) cur_tok.value, cur_tok.col, cur_tok.line);
					Node op = new OpNode(opeq_types[opeq_type_id], g, n, cur_tok.col, cur_tok.line);
					return new SetterNode(op, (String) cur_tok.value, cur_tok.col, cur_tok.line);
				}
			}
			this.tok_id = cp_tok_id - 1;
			this.advance();
			this.advance();
			
			TokenType[] opop1_types = new TokenType[] {
					TokenType.PLUS, TokenType.MINUS
			};
			int opop1_type_id = -1;
			i = 0;
			for (TokenType t:opop1_types) {
				if (t == this.current_token.type) {
					opop1_type_id = i;
					break;
				}
				i ++;
			}
			
			if(opop1_type_id != -1) {
				this.advance();
				if(this.current_token.type == opop1_types[opop1_type_id]) {
					TokenType op = this.current_token.type;
					Node g = new GetterNode((String) cur_tok.value, cur_tok.col, cur_tok.line);
					Node n = new NumberNode(1, -1, -1);
					Node opn = new OpNode(op, g, n, cur_tok.col, cur_tok.line);
					this.advance();
					return new SetterNode(opn, (String) cur_tok.value, cur_tok.col, cur_tok.line);
				}
			}
		}
		
		this.tok_id = cp_tok_id - 1;
		this.advance();
		
		return this.bin();
	}
	
	private Node bin() {
		Node left = this.comp();
		
		TokenType[] types = new TokenType[] {
			TokenType.OR, TokenType.AND
		};
		while (contains(this.current_token.type, types)) {
			Token oper = this.current_token;
			TokenType type = this.current_token.type;
			this.advance();
			Node right = this.comp();
			left = new OpNode(type, left, right, oper.col, oper.line);
		}
		
		return left;
	}
	
	private Node comp() {
		Node left = this.expr();
		
		TokenType[] types = new TokenType[] {
			TokenType.EQ, TokenType.INF, TokenType.NOTEQ, TokenType.SUP,
			TokenType.SUPEQ, TokenType.INFEQ
		};
		while (contains(this.current_token.type, types)) {
			Token oper = this.current_token;
			TokenType type = this.current_token.type;
			this.advance();
			Node right = this.expr();
			left = new OpNode(type, left, right, oper.col, oper.line);
		}
		
		return left;
	}
	
	private Node expr() {
		Node left = this.term();
		
		TokenType[] types = new TokenType[] {
			TokenType.PLUS, TokenType.MINUS
		};
		while (contains(this.current_token.type, types)) {
			Token oper = this.current_token;
			TokenType type = this.current_token.type;
			this.advance();
			Node right = this.term();
			left = new OpNode(type, left, right, oper.col, oper.line);
		}
		
		return left;
	}
	
	private Node term() {
		Node left = this.factor();

		TokenType[] types = new TokenType[] {
			TokenType.MUL, TokenType.DIV, TokenType.POW
		};
		while (contains(this.current_token.type, types)) {
			Token oper = this.current_token;
			TokenType type = this.current_token.type;
			this.advance();
			Node right = this.factor();
			left = new OpNode(type, left, right, oper.col, oper.line);
		}
		
		return left;
	}
	
	private Node factor() {
		Token tok = this.current_token;
		
		if (tok.type == TokenType.GLOBAL) {
			this.advance();
			return new GlobalizerNode(tok.col, tok.line, this.factor());
		}
		
		if (tok.type == TokenType.NUMBER) {
			this.advance();
			return new NumberNode((BigDecimal)tok.value, tok.col, tok.line);
		} else if (tok.type == TokenType.NAME) {
			this.advance();
			return this.listExpr(new GetterNode((String)tok.value, tok.col, tok.line));
	    } else if (tok.type == TokenType.MINUS) {
			this.advance();
			return new UnaryOpNode(tok.type, this.factor(), tok.col, tok.line);
		} else if (tok.type == TokenType.NOT) {
			this.advance();
			return new UnaryOpNode(tok.type, this.factor(), tok.col, tok.line);
		} else if (tok.type == TokenType.STRING) {
			this.advance();
			return this.listExpr(new StringNode(tok.col, tok.line, (String) tok.value));
		} else if (tok.type == TokenType.LPAREN) {
			this.advance();
			Node expr = this.bin();
			if (this.current_token.type == TokenType.RPAREN) {
				this.advance();
				return expr;
			} else {
				System.out.println("Missing right parenthesis");
				EntryPoint.raiseToken(tok);
				return null;
			}
		} else if (tok.type == TokenType.LHOOK) {
			this.advance();
			return this.buildArray();
		} else if (tok.type == TokenType.LCURLYBRACKET) {
			this.advance();
			return this.buildDict();
		} else if (tok.type == TokenType.TRUE) {
			this.advance();
			return new BooleanNode(tok.col, tok.line, true);
		} else if (tok.type == TokenType.FALSE) {
			this.advance();
			return new BooleanNode(tok.col, tok.line, false);
		}
		
		return null;
	}
	
	private Node buildArray() {
		ArrayBuilder node = new ArrayBuilder(this.current_token.col, this.current_token.line);
		
		TokenType lastType = this.current_token.type;
		while(this.advanceResult && lastType != TokenType.RHOOK) {
			Node expr = this.bin();
			lastType = this.current_token.type;
			
			this.advance();
			node.add(expr);
		}
		
		return node;
	}
	
	private Node buildDict() {
		DictBuilder node = new DictBuilder(this.current_token.col, this.current_token.line);
		
		TokenType lastType = this.current_token.type;
		while(this.advanceResult && lastType != TokenType.RCURLYBRACKET) {
			Node key_expr = this.bin();
			if (this.current_token.type != TokenType.TWO_POINTS) {
				System.out.println("Expected two points in dictionnary, you might have an error in your expression");
				EntryPoint.raiseToken(this.current_token);
				return null;
			}
			this.advance();
			Node obj_expr = this.bin();
			lastType = this.current_token.type;
			
			this.advance();
			node.put(key_expr, obj_expr);
		}
		
		return node;
	}
	
	private Node listExpr(Node node) {
		Token t = this.current_token;
		node = this.funcExpr(node, t);
		while (this.current_token != null && this.current_token.type == TokenType.LHOOK) {
			this.advance();
			
			Node n = this.bin();
			if (this.current_token.type != TokenType.RHOOK) {
				System.out.println("Syntax error: Missing right hook");
				EntryPoint.raiseToken(t);
				return null;
			}
			if (n == null) {
				System.out.println("Syntax error: Empty expression");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
			
			node = this.funcExpr(new ListGetterNode(t.col, t.line, node, n), t);
		}
		return node;
	}
	public Node funcExpr(Node node, Token t) {
		while (this.current_token != null && this.current_token.type == TokenType.LPAREN) {
			this.advance();

			ArrayList<Node> exprs = new ArrayList<Node>();
			HashMap<StringNode, Node> kw_exprs = new HashMap<>();
			
			TokenType lastType = this.current_token.type;
			while(this.advanceResult && lastType != TokenType.RPAREN) {
				
				Token name = null;
				
				if (this.current_token.type == TokenType.NAME
						&& this.next() != null
						&& this.next().type == TokenType.SET) {
					name = this.current_token;
					this.advance();
					this.advance();
				}
				Node expr = this.bin();
				lastType = this.current_token.type;
				if (lastType != TokenType.RPAREN) {
					this.advance();
				}
				
				if (name == null) {
					exprs.add(expr);
				} else {
					kw_exprs.put(new StringNode(-1, -1, (String) name.value), expr);
				}
			}
			
			if (lastType != TokenType.RPAREN) {
				System.out.println("Syntax error: Missing right parenthesies");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
			
			node = new FuncGetterNode(t.col, t.line, node, exprs, kw_exprs);
		}
		return node;
	}
	private Node listSetExpr(Node node) {
		Token t = this.current_token;
		while (this.current_token != null && this.current_token.type == TokenType.LHOOK) {
			this.advance();
			
			Node n = this.bin();
			if (this.current_token.type != TokenType.RHOOK) {
				System.out.println("Syntax error: Missing right hook");
				EntryPoint.raiseToken(t);
				return null;
			}
			if (n == null) {
				System.out.println("Syntax error: Empty expression");
				EntryPoint.raiseToken(t);
				return null;
			}
			this.advance();
			
			node = new ListSetterNode(t.col, t.line, node, n);
		}
		return node;
	}
		
}
