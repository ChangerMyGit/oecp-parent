<html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		
	
		<script type="text/javascript" src="fysp/ExpenseCheckBillWindow.js"></script>
	</head>
	<body>
		<table>
			<tr>
				<td >
					<table>
						<tr rownap>
							<td >
								aaa<input type="text" value="aaaa"/>
							</td>
							<td >
								aaa<input type="text" value="aaaa"/>
							</td>
						</tr>
						<tr rownap>
							<td >
								aaa<input type="text" value="aaaa"/>
							</td>
							<td >
								aaa<input type="text" value="aaaa"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
		
	</body>
</html>