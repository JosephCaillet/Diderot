<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="no"/>

	<xsl:variable name="goBackLinkPrefix" select="'--!--'"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
		<html>
			<head>
				<title>
					<xsl:value-of select="diderotProject/@name"/>
				</title>
				<link rel="stylesheet" href="style.css"/>
				<link rel="stylesheet" href="highlight_js/monokai-sublime.css"/>
				<script src="highlight_js/highlight.min.js"/>
				<script src="folding.js"/>
				<script>
					hljs.initHighlightingOnLoad();
					folder.setUpFoldingOnLoad(true);
				</script>
			</head>
			<body>
				<xsl:comment>
					Documentation generated with Diderot: https://github.com/JosephCaillet/Diderot
				</xsl:comment>
				<xsl:comment>
					Syntax highlighting provided by https://github.com/isagalaev/highlight.js
				</xsl:comment>
				<header>
					<img src="logo.png">
						<xsl:attribute name="alt">
							<xsl:value-of select="@name"/> project logo.
						</xsl:attribute>
					</img>
					<h1>
						<xsl:value-of select="diderotProject/@name"/>
					</h1>
				</header>
				<h2>
					<xsl:value-of select="diderotProject/@name"/>
				</h2>
				<p>Made by <xsl:value-of select="diderotProject/@authors"/>, for <xsl:value-of
						select="diderotProject/@company"/>.
				</p>
				<xsl:apply-templates/>
				<div class="expand">
					<span>&#8609; Expand all</span>
					<span>&#8607; Collapse all</span>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="pluginsProperties"/>

	<xsl:template match="diderotProject/description">
		<p>
			<xsl:call-template name="decodeNewLine">
				<xsl:with-param name="text" select="."/>
			</xsl:call-template>
		</p>
	</xsl:template>

	<xsl:template match="responseOutputFormat">
		<!--<p>-->
		<!--These routes can produce the following outputs:-->
		<!--<ul>-->
		<!--<xsl:for-each select="format">-->
		<!--<li>-->
		<!--<xsl:value-of select="."/>-->
		<!--</li>-->
		<!--</xsl:for-each>-->
		<!--</ul>-->
		<!--</p>-->
	</xsl:template>

	<xsl:template match="userDefinedProperties">
		<!--<xsl:if test="property">-->
		<!--<p>-->
		<!--The following routes' properties as been defined:-->
		<!--<ul>-->
		<!--<xsl:for-each select="property">-->
		<!--<li>-->
		<!--<xsl:value-of select="@name"/>-->
		<!--</li>-->
		<!--</xsl:for-each>-->
		<!--</ul>-->
		<!--</p>-->
		<!--</xsl:if>-->
	</xsl:template>

	<xsl:template match="/diderotProject/route">
		<h2>Routes summary</h2>
		<div class="routesSummarySection">
			<xsl:call-template name="printRoutesSummary">
				<xsl:with-param name="route" select="."/>
				<xsl:with-param name="tree" select="''"/>
			</xsl:call-template>
		</div>

		<h2>Routes details</h2>
		<div class="routesDetailsSection">
			<xsl:call-template name="printRoutesDetails">
				<xsl:with-param name="route" select="."/>
				<xsl:with-param name="tree" select="''"/>
			</xsl:call-template>
		</div>
	</xsl:template>

	<xsl:template name="printRoutesSummary">
		<xsl:param name="route"/>
		<xsl:param name="tree"/>
		<span class="routeSummaryName">
			<xsl:choose>
				<xsl:when test="$route/methods/method or $route/description != ''">
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="concat('#', $tree)"/>
						</xsl:attribute>
						<xsl:attribute name="id">
							<xsl:value-of select="concat($goBackLinkPrefix, $tree)"/>
						</xsl:attribute>
						<xsl:value-of select="@name"/>
					</a>
				</xsl:when>
				<xsl:when test="true()">
					<xsl:value-of select="@name"/>
				</xsl:when>
			</xsl:choose>

			<xsl:for-each select="$route/routes/route">
				<xsl:call-template name="printRoutesSummary">
					<xsl:with-param name="route" select="."/>
					<xsl:with-param name="tree" select="concat($tree, '/', @name)"/>
				</xsl:call-template>
			</xsl:for-each>
		</span>
	</xsl:template>

	<xsl:template name="printRoutesDetails">
		<xsl:param name="route"/>
		<xsl:param name="tree"/>

		<xsl:if test="$route/methods/method or $route/description != ''">
			<div class="routeDetails">
				<a>
					<xsl:attribute name="href">
						<xsl:value-of select="concat('#', $goBackLinkPrefix, $tree)"/>
					</xsl:attribute>
					&#8679;
				</a>
				<h3>
					<xsl:attribute name="id">
						<xsl:value-of select="$tree"/>
					</xsl:attribute>
					<xsl:if test="($route/methods/method or $route/description != '') and $tree = ''">
						/
					</xsl:if>
					<xsl:value-of select="$tree"/>
				</h3>
				<span>
					<xsl:value-of select="count($route/methods/method)"/> method(s)
				</span>
				<xsl:call-template name="printRouteDetail"/>
			</div>
		</xsl:if>

		<xsl:for-each select="$route/routes/route">
			<xsl:call-template name="printRoutesDetails">
				<xsl:with-param name="route" select="."/>
				<xsl:with-param name="tree" select="concat($tree, '/', @name)"/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="printRouteDetail">
		<div>
			<p>
				<xsl:call-template name="decodeNewLine">
					<xsl:with-param name="text" select="description"/>
				</xsl:call-template>
			</p>

			<xsl:if test="methods/method">
				<xsl:for-each select="methods/method">
					<div>
						<xsl:variable name="baseClassName" select="'methodContainer'"/>
						<xsl:choose>
							<xsl:when test="@name = 'GET'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> getMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'HEAD'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> headMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'POST'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> postMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'OPTIONS'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> optionsMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'CONNECT'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> connectMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'TRACE'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> traceMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'PUT'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> putMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'PATCH'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> patchMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="@name = 'DELETE'">
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/> deleteMethod
								</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="class">
									<xsl:value-of select="$baseClassName"/>
								</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
						<h4>
							<xsl:value-of select="@name"/>
						</h4>

						<div class="method">
							<p>
								<xsl:call-template name="decodeNewLine">
									<xsl:with-param name="text" select="description"/>
								</xsl:call-template>
							</p>

							<xsl:if test="userDefinedProperties/value">
								<div class="propertyContainer">
									<h5>Properties</h5>
									<table>
										<xsl:for-each select="userDefinedProperties/value">
											<tr>
												<td>
													<xsl:value-of select="@property"/>:
												</td>
												<td>
													<xsl:value-of select="."/>
												</td>
											</tr>
										</xsl:for-each>
									</table>
								</div>
							</xsl:if>

							<xsl:if test="parameters/parameter">
								<div>
									<h5>Parameter(s)</h5>
									<table class="params">
										<tr>
											<th>Name</th>
											<th>Required</th>
											<th>Description</th>
										</tr>
										<xsl:for-each select="parameters/parameter">
											<tr>
												<td>
													<xsl:value-of select="@name"/>
												</td>
												<td>
													<xsl:if test="@required = 'true'">
														&#10004;
													</xsl:if>
												</td>
												<td>
													<xsl:value-of select="@description"/>
												</td>
											</tr>
										</xsl:for-each>
									</table>
								</div>
							</xsl:if>

							<xsl:if test="responses/response">
								<h5>Response(s)</h5>
								<div class="responsesContainer">
									<xsl:for-each select="responses/response">
										<div class="responseContainer">
											<h6>
												<xsl:value-of select="@name"/>
											</h6>
											<div class="response">
												<pre>
													<xsl:call-template name="decodeNewLine">
														<xsl:with-param name="text" select="description"/>
													</xsl:call-template>
												</pre>
												<i>
													Output:
													<xsl:value-of select="@outputFormat"/>
												</i>
												<xsl:if test="string-length(outputSchema) != 0">
													<pre>
														<code>
															<xsl:attribute name="class">
																<xsl:value-of select="@outputFormat"/>
															</xsl:attribute>
															<xsl:call-template name="decodeNewLine">
																<xsl:with-param name="text" select="outputSchema"/>
															</xsl:call-template>
														</code>
													</pre>
												</xsl:if>
											</div>
										</div>
									</xsl:for-each>
								</div>
							</xsl:if>
						</div>
					</div>
				</xsl:for-each>
			</xsl:if>
		</div>
	</xsl:template>

	<xsl:template name="decodeNewLine">
		<xsl:param name="text"/>
		<xsl:call-template name="replaceString">
			<xsl:with-param name="text" select="$text"/>
			<xsl:with-param name="replace" select="'&amp;#xA;'"/>
			<xsl:with-param name="by" select="'&lt;br&gt;'"/>
		</xsl:call-template>
	</xsl:template>

	<!--by Mads Hansen : http://stackoverflow.com/questions/7520762/xslt-1-0-string-replace-function-->
	<xsl:template name="replaceString">
		<xsl:param name="text"/>
		<xsl:param name="replace"/>
		<xsl:param name="by"/>
		<xsl:choose>
			<xsl:when test="contains($text,$replace)">
				<xsl:value-of select="substring-before($text,$replace)"/>
				<xsl:value-of disable-output-escaping="yes" select="$by"/>
				<xsl:call-template name="replaceString">
					<xsl:with-param name="text"
									select="substring-after($text,$replace)"/>
					<xsl:with-param name="replace" select="$replace"/>
					<xsl:with-param name="by" select="$by"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>