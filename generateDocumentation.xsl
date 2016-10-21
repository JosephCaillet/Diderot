<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
		<html>
			<head>
				<title><xsl:value-of select="diderotProject/@name"/></title>
				<link rel="stylesheet" href="style.css"/>
			</head>
			<body>
				<h1><xsl:value-of select="diderotProject/@name"/></h1>
				<h2>Project description</h2>
				<p>Made by <xsl:value-of select="diderotProject/@authors"/>, for <xsl:value-of select="diderotProject/@company"/>.</p>
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="diderotProject/description">
		<p>
			<xsl:call-template name="decodeNewLine">
				<xsl:with-param name="text" select="."/>
			</xsl:call-template>
		</p>
	</xsl:template>

	<xsl:template match="responseOutputFormat">
		<p>
			These routes can produce the following outputs:
			<ul>
				<xsl:for-each select="format">
					<li>
						<xsl:value-of select="."/>
					</li>
				</xsl:for-each>
			</ul>
		</p>
	</xsl:template>

	<xsl:template match="userDefinedProperties">
		<p>
			The following routes' properties as been defined:
			<ul>
				<xsl:for-each select="property">
					<li>
						<xsl:value-of select="@name"/>
					</li>
				</xsl:for-each>
			</ul>
		</p>
	</xsl:template>

	<xsl:template match="/diderotProject/route">
		<h2>Routes summary</h2>
		<div>
			<xsl:call-template name="printRoutesSummary">
				<xsl:with-param name="route" select="."/>
			</xsl:call-template>
		</div>

		<h2>Routes details</h2>
		<div>
			<xsl:call-template name="printRoutesDetails">
				<xsl:with-param name="route" select="."/>
				<xsl:with-param name="tree" select="''"/>
			</xsl:call-template>
		</div>
	</xsl:template>

	<xsl:template name="printRoutesSummary">
		<xsl:param name="route"/>
		<span class="routeSummary">
			<xsl:choose>
				<xsl:when test="$route/methods/method or $route/description != ''">
					<a href="#">
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
				</xsl:call-template>
			</xsl:for-each>
		</span>
	</xsl:template>

	<xsl:template name="printRoutesDetails">
		<xsl:param name="route"/>
		<xsl:param name="tree"/>

		<xsl:if test="$route/methods/method or $route/description != ''">
			<span class="routeDetails">
				<h3>
					<xsl:if test="($route/methods/method or $route/description != '') and $tree = ''">
						/
					</xsl:if>
					<xsl:value-of select="$tree"/>
				</h3>
				<a>Go to top</a>
				<xsl:call-template name="printRouteDetail"/>
			</span>
		</xsl:if>

		<xsl:for-each select="$route/routes/route">
			<xsl:call-template name="printRoutesDetails">
				<xsl:with-param name="route" select="."/>
				<xsl:with-param name="tree" select="concat($tree, '/', @name)"/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="printRouteDetail">
		<p>
			<xsl:call-template name="decodeNewLine">
				<xsl:with-param name="text" select="description"/>
			</xsl:call-template>
		</p>

		<xsl:if test="methods/method">
			<xsl:for-each select="methods/method">
				<div class="method">
					<h4>
						<xsl:value-of select="@name"/>
					</h4>
					<p>
						<xsl:call-template name="decodeNewLine">
							<xsl:with-param name="text" select="description"/>
						</xsl:call-template>
					</p>

					<xsl:if test="parameters/parameter">
						<div>
							<h5>Parameters</h5>
							<table>
								<tr>
									<th>Name</th>
									<th>Required</th>
									<th>Description</th>
								</tr>
								<xsl:for-each select="parameters/parameter">
									<tr>
										<td>
											<xsl:value-of select="@name"/></td>
										<td>
											<xsl:if test="@required = 'true'">
												&#10003;
											</xsl:if>
										</td>
										<td>
											<xsl:value-of select="@description"/></td>
									</tr>
								</xsl:for-each>
							</table>
						</div>
					</xsl:if>

					<xsl:if test="responses/response">
						<div>
							<h5>Responses</h5>
							<xsl:for-each select="responses/response">
								<div>
									<h6>
										<xsl:value-of select="@name"/>
									</h6>
									<span>
										<xsl:value-of select="@outputFormat"/>
									</span>
									<p>
										<xsl:call-template name="decodeNewLine">
											<xsl:with-param name="text" select="description"/>
										</xsl:call-template>
									</p>
									<p>
										<xsl:call-template name="decodeNewLine">
											<xsl:with-param name="text" select="outputSchema"/>
										</xsl:call-template>
									</p>
								</div>
							</xsl:for-each>
						</div>
					</xsl:if>
				</div>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<xsl:template name="decodeNewLine">
		<xsl:param name="text"/>
		<pre>
			<xsl:value-of select="translate($text, '&amp;#xA;', '&#xA;')"/>
		</pre>
	</xsl:template>
</xsl:stylesheet>