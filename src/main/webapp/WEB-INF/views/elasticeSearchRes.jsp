<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Search Dashboard</title>

                <style>
                    body {
                        font-family: Arial;
                        background: #f4f6f9;
                    }

                    .card {
                        background: white;
                        padding: 20px;
                        margin: 20px auto;
                        width: 600px;
                        border-radius: 10px;
                        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                    }

                    .badge {
                        padding: 5px 10px;
                        border-radius: 5px;
                        color: white;
                        font-weight: bold;
                    }

                    .green {
                        background: green;
                    }

                    .red {
                        background: red;
                    }

                    .gray {
                        background: gray;
                    }

                    code {
                        background: #eee;
                        padding: 3px 6px;
                        border-radius: 5px;
                    }

                    .title {
                        display: flex;
                        align-items: center;
                        gap: 8px;
                    }
                </style>
            </head>

            <body>

                <div class="card">

                    <h2>🔍 ${searchEngineName} Dashboard</h2>

                    <h3 style="margin-top: 10px;">
                        📦 Action: Index Users (Sync to Search Engine)
                    </h3>

                    <p>
                        <span class="badge ${statusColor}">
                            ${statusBadge}
                        </span>
                    </p>

                    <h3>Result</h3>
                    <p>${result}</p>

                    <hr>

                    <p><b>Environment:</b> ${environment}</p>
                    <p><b>Search Engine:</b> 🔍 ${searchEngineName}</p>
                    <p><b>Endpoint:</b> <code>${searchEngineUrl}</code></p>

                </div>

            </body>

            </html>